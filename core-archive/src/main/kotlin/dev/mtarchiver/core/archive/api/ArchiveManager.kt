package dev.mtarchiver.core.archive.api

import java.io.File
import kotlinx.coroutines.flow.Flow

interface ArchiveManager {
    /**
     * Get all registered archive formats.
     */
    fun getFormats(): List<ArchiveFormat>

    /**
     * Get format by file extension.
     */
    fun getFormatByExtension(extension: String): ArchiveFormat?

    /**
     * Get format that can handle the file.
     */
    fun getFormatForFile(file: File): ArchiveFormat?

    /**
     * List entries in archive.
     */
    suspend fun listEntries(file: File): List<ArchiveEntry>

    /**
     * Extract archive.
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
        format: String = "zip",
        compressionLevel: Int = 6,
        password: String? = null,
        progressListener: ProgressListener? = null
    ): CreateResult

    /**
     * Test archive integrity.
     */
    suspend fun test(archiveFile: File): TestResult

    /**
     * Register a custom format plugin.
     */
    fun registerFormat(format: ArchiveFormat)

    /**
     * Unregister a format plugin.
     */
    fun unregisterFormat(formatName: String)

    /**
     * Observe archive operations events.
     */
    fun observeOperations(): Flow<ArchiveOperation>
}

sealed class ArchiveOperation {
    data class Extracting(val fileName: String, val progress: Float) : ArchiveOperation()
    data class Creating(val fileName: String, val progress: Float) : ArchiveOperation()
    data class Completed(val operationType: String, val success: Boolean) : ArchiveOperation()
}
