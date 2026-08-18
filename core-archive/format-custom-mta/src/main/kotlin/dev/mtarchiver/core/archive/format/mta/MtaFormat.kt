package dev.mtarchiver.core.archive.format.mta

import dev.mtarchiver.core.archive.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.facebook.zstd.ZstdCompressingOutputStream
import com.facebook.zstd.ZstdDecompressingInputStream
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * MT Archiver custom format (.mta) using Zstandard compression with metadata.
 * Format: [Header][Metadata][Compressed Content]
 */
class MtaFormat : ArchiveFormat {
    override val name = "MTA"
    override val extensions = listOf("mta")
    override val mimeTypes = listOf("application/x-mta")

    private val MAGIC = "MTA1".toByteArray()

    override fun canHandle(file: File): Boolean {
        return file.extension.lowercase() == "mta"
    }

    override suspend fun listEntries(file: File): List<ArchiveEntry> {
        return withContext(Dispatchers.IO) {
            try {
                val input = DataInputStream(BufferedInputStream(FileInputStream(file)))
                val magic = ByteArray(4)
                input.readFully(magic)

                if (!magic.contentEquals(MAGIC)) {
                    return@withContext emptyList()
                }

                val entryCount = input.readInt()
                val entries = mutableListOf<ArchiveEntry>()

                repeat(entryCount) {
                    val nameLength = input.readInt()
                    val name = String(ByteArray(nameLength).apply { input.readFully(this) })
                    val isDir = input.readBoolean()
                    val size = input.readLong()
                    val compSize = input.readLong()
                    val modified = input.readLong()

                    entries.add(
                        ArchiveEntry(
                            name = name,
                            isDirectory = isDir,
                            size = size,
                            compressedSize = compSize,
                            lastModified = modified
                        )
                    )
                }

                input.close()
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
                val input = DataInputStream(BufferedInputStream(FileInputStream(archiveFile)))
                val magic = ByteArray(4)
                input.readFully(magic)

                if (!magic.contentEquals(MAGIC)) {
                    return@withContext ExtractResult(false, 0, 1, listOf("Invalid MTA format"))
                }

                val entryCount = input.readInt()
                var extracted = 0
                var failed = 0

                // Skip metadata for now, decompress content
                val zstdInput = ZstdDecompressingInputStream(input)
                val dataInput = DataInputStream(zstdInput)

                repeat(entryCount) { index ->
                    try {
                        val nameLength = dataInput.readInt()
                        val name = String(ByteArray(nameLength).apply { dataInput.readFully(this) })
                        val isDir = dataInput.readBoolean()
                        val size = dataInput.readLong()

                        val targetFile = File(destinationDir, name)
                        if (isDir) {
                            targetFile.mkdirs()
                        } else {
                            targetFile.parentFile?.mkdirs()
                            val content = ByteArray(size.toInt())
                            dataInput.readFully(content)
                            targetFile.writeBytes(content)
                        }

                        extracted++
                        progressListener?.onProgress(index + 1, entryCount, name)
                    } catch (e: Exception) {
                        failed++
                    }
                }

                zstdInput.close()
                input.close()
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
                val output = DataOutputStream(BufferedOutputStream(FileOutputStream(archiveFile)))
                output.write(MAGIC)
                output.writeInt(sourceFiles.size)

                val zstdOutput = ZstdCompressingOutputStream(output)
                val dataOutput = DataOutputStream(zstdOutput)

                sourceFiles.forEachIndexed { index, file ->
                    writeFileToMta(dataOutput, file, "")
                    progressListener?.onProgress(index + 1, sourceFiles.size, file.name)
                }

                zstdOutput.close()
                output.close()
                CreateResult(true, archiveFile.length())
            } catch (e: Exception) {
                CreateResult(false, 0, listOf(e.message ?: "Unknown error"))
            }
        }
    }

    override suspend fun test(archiveFile: File): TestResult {
        return withContext(Dispatchers.IO) {
            try {
                val input = DataInputStream(BufferedInputStream(FileInputStream(archiveFile)))
                val magic = ByteArray(4)
                input.readFully(magic)
                input.close()
                TestResult(magic.contentEquals(MAGIC))
            } catch (e: Exception) {
                TestResult(false, listOf(e.message ?: "Invalid MTA file"))
            }
        }
    }

    private fun writeFileToMta(
        output: DataOutputStream,
        file: File,
        base: String
    ) {
        val entryName = if (base.isEmpty()) file.name else "$base/${file.name}"
        if (file.isDirectory) {
            file.listFiles()?.forEach {
                writeFileToMta(output, it, entryName)
            }
        } else {
            val name = entryName.toByteArray()
            output.writeInt(name.size)
            output.write(name)
            output.writeBoolean(false) // not directory
            output.writeLong(file.length())
            file.inputStream().use { fis ->
                fis.copyTo(output)
            }
        }
    }
}
