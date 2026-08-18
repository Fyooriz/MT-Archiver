package dev.mtarchiver.core.archive.format.tar

import dev.mtarchiver.core.archive.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class TarFormat : ArchiveFormat {
    override val name = "TAR"
    override val extensions = listOf("tar", "tar.gz", "tgz")
    override val mimeTypes = listOf("application/x-tar", "application/gzip")

    override fun canHandle(file: File): Boolean {
        val ext = file.extension.lowercase()
        return ext in listOf("tar", "tgz") || file.name.endsWith(".tar.gz")
    }

    override suspend fun listEntries(file: File): List<ArchiveEntry> {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream = FileInputStream(file)
                val bufferedInput = BufferedInputStream(inputStream)
                val gzipInput = if (isGzipped(file)) {
                    GzipCompressorInputStream(bufferedInput)
                } else {
                    bufferedInput
                }

                val tarInput = TarArchiveInputStream(gzipInput)
                val entries = mutableListOf<ArchiveEntry>()
                var entry: TarArchiveEntry? = tarInput.nextTarEntry

                while (entry != null) {
                    entries.add(
                        ArchiveEntry(
                            name = entry.name,
                            isDirectory = entry.isDirectory,
                            size = entry.size,
                            compressedSize = 0,
                            lastModified = entry.modTime.time,
                            crc = null
                        )
                    )
                    entry = tarInput.nextTarEntry
                }

                tarInput.close()
                entries
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
                val inputStream = FileInputStream(archiveFile)
                val bufferedInput = BufferedInputStream(inputStream)
                val gzipInput = if (isGzipped(archiveFile)) {
                    GzipCompressorInputStream(bufferedInput)
                } else {
                    bufferedInput
                }

                val tarInput = TarArchiveInputStream(gzipInput)
                var extracted = 0
                var failed = 0
                var index = 0
                var entry: TarArchiveEntry? = tarInput.nextTarEntry

                while (entry != null) {
                    try {
                        val targetFile = File(destinationDir, entry.name)
                        if (entry.isDirectory) {
                            targetFile.mkdirs()
                        } else {
                            targetFile.parentFile?.mkdirs()
                            targetFile.outputStream().use { fos ->
                                tarInput.copyTo(fos)
                            }
                        }
                        extracted++
                        progressListener?.onProgress(index + 1, 0, entry.name)
                    } catch (e: Exception) {
                        failed++
                    }
                    index++
                    entry = tarInput.nextTarEntry
                }

                tarInput.close()
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
                val fileOutput = FileOutputStream(archiveFile)
                val bufferedOutput = BufferedOutputStream(fileOutput)
                val gzipOutput = GzipCompressorOutputStream(bufferedOutput)
                val tarOutput = TarArchiveOutputStream(gzipOutput)

                sourceFiles.forEachIndexed { index, file ->
                    addFileToTar(tarOutput, file, "")
                    progressListener?.onProgress(index + 1, sourceFiles.size, file.name)
                }

                tarOutput.close()
                CreateResult(true, archiveFile.length())
            } catch (e: Exception) {
                CreateResult(false, 0, listOf(e.message ?: "Unknown error"))
            }
        }
    }

    override suspend fun test(archiveFile: File): TestResult {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream = FileInputStream(archiveFile)
                val bufferedInput = BufferedInputStream(inputStream)
                val gzipInput = if (isGzipped(archiveFile)) {
                    GzipCompressorInputStream(bufferedInput)
                } else {
                    bufferedInput
                }
                val tarInput = TarArchiveInputStream(gzipInput)
                tarInput.nextTarEntry
                tarInput.close()
                TestResult(true)
            } catch (e: Exception) {
                TestResult(false, listOf(e.message ?: "Invalid TAR file"))
            }
        }
    }

    private fun isGzipped(file: File): Boolean {
        return file.extension.lowercase() in listOf("gz", "tgz") || file.name.endsWith(".tar.gz")
    }

    private fun addFileToTar(
        tarOutput: TarArchiveOutputStream,
        file: File,
        base: String
    ) {
        val entryName = if (base.isEmpty()) file.name else "$base/${file.name}"
        if (file.isDirectory) {
            file.listFiles()?.forEach {
                addFileToTar(tarOutput, it, entryName)
            }
        } else {
            val entry = TarArchiveEntry(file, entryName)
            tarOutput.putArchiveEntry(entry)
            file.inputStream().use { fis ->
                fis.copyTo(tarOutput)
            }
            tarOutput.closeArchiveEntry()
        }
    }
}
