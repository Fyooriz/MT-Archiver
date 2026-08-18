package dev.mtarchiver.core.archive.format.zip

import dev.mtarchiver.core.archive.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.model.ZipParameters
import net.lingala.zip4j.model.enums.CompressionMethod
import net.lingala.zip4j.model.enums.EncryptionMethod
import java.io.File

class ZipFormat : ArchiveFormat {
    override val name = "ZIP"
    override val extensions = listOf("zip")
    override val mimeTypes = listOf("application/zip", "application/x-zip-compressed")

    override fun canHandle(file: File): Boolean {
        return file.extension.lowercase() == "zip"
    }

    override suspend fun listEntries(file: File): List<ArchiveEntry> {
        return withContext(Dispatchers.IO) {
            try {
                val zipFile = ZipFile(file)
                zipFile.fileHeaders.map { header ->
                    ArchiveEntry(
                        name = header.fileName,
                        isDirectory = header.isDirectory,
                        size = header.uncompressedSize,
                        compressedSize = header.compressedSize,
                        lastModified = header.lastModifiedTime,
                        crc = null
                    )
                }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    override suspend fun extract(
        archiveFile: File,
        destinationDir: File,
        password: String?,
        progressListener: ProgressListener?
    ): ExtractResult {
        return withContext(Dispatchers.IO) {
            try {
                destinationDir.mkdirs()
                val zipFile = ZipFile(archiveFile)
                if (password != null) {
                    zipFile.setPassword(password.toCharArray())
                }

                val entries = zipFile.fileHeaders.size
                var extracted = 0
                var failed = 0

                zipFile.fileHeaders.forEachIndexed { index, header ->
                    try {
                        zipFile.extractFile(header, destinationDir.absolutePath)
                        extracted++
                        progressListener?.onProgress(index + 1, entries, header.fileName)
                    } catch (e: Exception) {
                        failed++
                    }
                }

                ExtractResult(failed == 0, extracted, failed)
            } catch (e: Exception) {
                ExtractResult(false, 0, 1, listOf(e.message ?: "Unknown error"))
            }
        }
    }

    override suspend fun create(
        sourceFiles: List<File>,
        archiveFile: File,
        compressionLevel: Int,
        password: String?,
        progressListener: ProgressListener?
    ): CreateResult {
        return withContext(Dispatchers.IO) {
            try {
                val zipFile = ZipFile(archiveFile)
                if (password != null) {
                    zipFile.setPassword(password.toCharArray())
                }

                sourceFiles.forEachIndexed { index, file ->
                    val params = ZipParameters().apply {
                        compressionMethod = CompressionMethod.DEFLATE
                        isEncryptFiles = password != null
                        encryptionMethod = EncryptionMethod.AES
                    }

                    if (file.isDirectory) {
                        zipFile.addFolder(file, params)
                    } else {
                        zipFile.addFile(file, params)
                    }
                    progressListener?.onProgress(index + 1, sourceFiles.size, file.name)
                }

                CreateResult(true, archiveFile.length())
            } catch (e: Exception) {
                CreateResult(false, 0, listOf(e.message ?: "Unknown error"))
            }
        }
    }

    override suspend fun test(archiveFile: File): TestResult {
        return withContext(Dispatchers.IO) {
            try {
                val zipFile = ZipFile(archiveFile)
                zipFile.isValidZipFile
                TestResult(true)
            } catch (e: Exception) {
                TestResult(false, listOf(e.message ?: "Invalid ZIP file"))
            }
        }
    }
}
