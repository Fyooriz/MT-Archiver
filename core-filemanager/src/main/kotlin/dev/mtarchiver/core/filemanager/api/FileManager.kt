package dev.mtarchiver.core.filemanager.api

import java.io.File

/**
 * File Manager interface for file operations.
 */
interface FileManager {
    /**
     * List files in directory.
     */
    suspend fun listFiles(directory: File): List<FileInfo>

    /**
     * Copy file.
     */
    suspend fun copyFile(source: File, destination: File): Boolean

    /**
     * Move file.
     */
    suspend fun moveFile(source: File, destination: File): Boolean

    /**
     * Delete file securely (DoD 5220.22-M standard).
     */
    suspend fun secureDelete(file: File): Boolean

    /**
     * Get directory size.
     */
    suspend fun getDirectorySize(directory: File): Long

    /**
     * Search files by pattern.
     */
    suspend fun searchFiles(directory: File, pattern: String): List<File>

    /**
     * Calculate file hash.
     */
    suspend fun calculateHash(file: File): String
}

data class FileInfo(
    val file: File,
    val name: String,
    val size: Long,
    val lastModified: Long,
    val isDirectory: Boolean,
    val permissions: String,
    val isHidden: Boolean,
    val mimeType: String? = null
)
