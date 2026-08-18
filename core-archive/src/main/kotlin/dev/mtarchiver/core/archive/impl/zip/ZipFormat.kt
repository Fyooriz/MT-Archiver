package dev.mtarchiver.core.archive.impl.zip

import dev.mtarchiver.core.archive.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.model.FileHeader
import net.lingala.zip4j.model.ZipParameters
import net.lingala.zip4j.model.enums.CompressionLevel
import net.lingala.zip4j.model.enums.CompressionMethod
import java.io.File

class ZipFormat : ArchiveFormat {
    override val name: String = "zip"
    override val extensions: List<String> = listOf("zip")
    override val mimeTypes: List<String> = listOf("application/zip")

    override fun canHandle(file: File): Boolean {
        return file.extension.lowercase() in extensions
    }

    override suspend fun listEntries(file: File): List<ArchiveEntry> = withContext(Dispatchers.IO) {
        val zip = ZipFile(file)
        val headers: List<FileHeader> = zip.fileHeaders
        headers.map { fh ->
            ArchiveEntry(
                name = fh.fileName,
                isDirectory = fh.isDirectory,
                size = fh.uncompressedSize,
                compressedSize = fh.compressedSize,
                lastModified = fh.lastModifiedTime?.time ?: 0L,
                crc = fh.crc
            )
        }
    }

    // Protect against zip-slip by validating canonical paths
    private fun isPathSafe(destinationDir: File, entryName: String): Boolean {
        val destFile = File(destinationDir, entryName)
        val destCanonical = try { destFile.canonicalPath } catch (e: Exception) { return false }
        val destDirCanonical = try { destinationDir.canonicalPath } catch (e: Exception) { return false }
        return destCanonical.startsWith(destDirCanonical + File.separator) || destCanonical == destDirCanonical
    }

    override suspend fun extract(
        archiveFile: File,
        destinationDir: File,
        password: String?,
        progressListener: ProgressListener?
    ): ExtractResult = withContext(Dispatchers.IO) {
        val errors = mutableListOf<String>()
        try {
            val zip = if (!password.isNullOrEmpty()) ZipFile(archiveFile, password.toCharArray()) else ZipFile(archiveFile)
            val headers = zip.fileHeaders
            val total = headers.size
            var extracted = 0
            var failed = 0

            headers.forEachIndexed { idx, header ->
                try {
                    val entryName = header.fileName

                    // Security: prevent zip-slip (path traversal)
                    if (!isPathSafe(destinationDir, entryName)) {
                        failed++
                        errors.add("Skipped unsafe entry: $entryName")
                        progressListener?.onProgress(extracted + failed, total, entryName)
                        return@forEachIndexed
                    }

                    val outFile = File(destinationDir, entryName)
                    if (header.isDirectory) {
                        outFile.mkdirs()
                    } else {
                        // ensure parent dirs exist
                        outFile.parentFile?.let { parent -> if (!parent.exists()) parent.mkdirs() }
                        zip.extractFile(header, destinationDir.absolutePath)
                    }
                    extracted++
                } catch (e: Exception) {
                    failed++
                    errors.add("Failed to extract ${header.fileName}: ${e.message}")
                }
                progressListener?.onProgress(extracted + failed, total, header.fileName)
            }

            ExtractResult(success = failed == 0, extractedCount = extracted, failedCount = failed, errors = errors)
        } catch (e: Exception) {
            ExtractResult(false, 0, 1, listOf(e.message ?: "Unknown error"))
        }
    }

    override suspend fun create(
        sourceFiles: List<File>,
        archiveFile: File,
        compressionLevel: Int,
        password: String?,
        progressListener: ProgressListener?
    ): CreateResult = withContext(Dispatchers.IO) {
        try {
            val zip = ZipFile(archiveFile)
            val params = ZipParameters()
            params.compressionMethod = CompressionMethod.DEFLATE
            // Map compression level 0..9 to Zip4j CompressionLevel
            params.compressionLevel = when (compressionLevel.coerceIn(0, 9)) {
                0 -> CompressionLevel.NO_COMPRESSION
                in 1..3 -> CompressionLevel.FASTEST
                in 4..6 -> CompressionLevel.FAST
                in 7..8 -> CompressionLevel.NORMAL
                else -> CompressionLevel.MAXIMUM
            }

            if (!password.isNullOrEmpty()) {
                zip.setPassword(password.toCharArray())
                params.isEncryptFiles = true
            }

            // Add files
            val total = sourceFiles.size
            var added = 0
            sourceFiles.forEach { f ->
                if (f.isDirectory) {
                    zip.addFolder(f, params)
                } else {
                    zip.addFile(f, params)
                }
                added++
                progressListener?.onProgress(added, total, f.name)
            }

            val archiveSize = archiveFile.length()
            CreateResult(success = true, archiveSize = archiveSize, errors = emptyList())
        } catch (e: Exception) {
            CreateResult(false, 0, listOf(e.message ?: "Unknown error"))
        }
    }

    override suspend fun test(archiveFile: File): TestResult = withContext(Dispatchers.IO) {
        try {
            val zip = ZipFile(archiveFile)
            // Attempt to read headers
            val headers = zip.fileHeaders
            TestResult(isValid = true, errors = emptyList())
        } catch (e: Exception) {
            TestResult(false, listOf(e.message ?: "Unknown error"))
        }
    }
}
