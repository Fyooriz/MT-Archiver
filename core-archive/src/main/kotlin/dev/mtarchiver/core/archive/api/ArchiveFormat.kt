package dev.mtarchiver.core.archive.api

import java.io.File

/**
 * Plugin SPI for archive format support.
 * Implementations should be stateless and thread-safe.
 */
interface ArchiveFormat {
    val name: String
    val extensions: List<String>
    val mimeTypes: List<String>

    /**
     * Check if this format can handle the given file.
     */
    fun canHandle(file: File): Boolean

    /**
     * List entries in archive.
     */
    suspend fun listEntries(file: File): List<ArchiveEntry>

    /**
     * Extract archive to destination.
     */
    suspend fun extract(
        archiveFile: File,
        destinationDir: File,
        password: String? = null,
        progressListener: ProgressListener? = null
    ): ExtractResult

    /**
     * Create archive from files.
     */
    suspend fun create(
        sourceFiles: List<File>,
        archiveFile: File,
        compressionLevel: Int = 6,
        password: String? = null,
        progressListener: ProgressListener? = null
    ): CreateResult

    /**
     * Test archive integrity.
     */
    suspend fun test(archiveFile: File): TestResult
}

data class ArchiveEntry(
    val name: String,
    val isDirectory: Boolean,
    val size: Long,
    val compressedSize: Long,
    val lastModified: Long,
    val crc: Long? = null
)

data class ExtractResult(
    val success: Boolean,
    val extractedCount: Int,
    val failedCount: Int,
    val errors: List<String> = emptyList()
)

data class CreateResult(
    val success: Boolean,
    val archiveSize: Long,
    val errors: List<String> = emptyList()
)

data class TestResult(
    val isValid: Boolean,
    val errors: List<String> = emptyList()
)

interface ProgressListener {
    fun onProgress(current: Int, total: Int, fileName: String)
}
