package dev.mtarchiver.core.cloud.api

import java.io.File

/**
 * Plugin SPI for cloud storage provider support.
 */
interface CloudProvider {
    val name: String
    val icon: String? // resource ID or URL

    /**
     * Authenticate with cloud provider.
     */
    suspend fun authenticate(credentials: AuthCredentials): AuthResult

    /**
     * Check if authenticated.
     */
    suspend fun isAuthenticated(): Boolean

    /**
     * Logout.
     */
    suspend fun logout()

    /**
     * List files in directory.
     */
    suspend fun listFiles(path: String = "/"): List<CloudFile>

    /**
     * Download file.
     */
    suspend fun downloadFile(remotePath: String, localFile: File): DownloadResult

    /**
     * Upload file.
     */
    suspend fun uploadFile(localFile: File, remotePath: String): UploadResult

    /**
     * Delete file/folder.
     */
    suspend fun delete(path: String): DeleteResult

    /**
     * Create folder.
     */
    suspend fun createFolder(path: String): CreateFolderResult

    /**
     * Rename file/folder.
     */
    suspend fun rename(path: String, newName: String): RenameResult
}

data class AuthCredentials(
    val username: String? = null,
    val password: String? = null,
    val token: String? = null,
    val refreshToken: String? = null,
    val apiKey: String? = null,
    val url: String? = null,
    val port: Int? = null
)

data class AuthResult(
    val success: Boolean,
    val error: String? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null
)

data class CloudFile(
    val name: String,
    val path: String,
    val isDirectory: Boolean,
    val size: Long,
    val lastModified: Long,
    val downloadUrl: String? = null,
    val mimeType: String? = null
)

data class DownloadResult(
    val success: Boolean,
    val error: String? = null,
    val downloadedSize: Long = 0
)

data class UploadResult(
    val success: Boolean,
    val error: String? = null,
    val uploadedSize: Long = 0
)

data class DeleteResult(
    val success: Boolean,
    val error: String? = null
)

data class CreateFolderResult(
    val success: Boolean,
    val error: String? = null
)

data class RenameResult(
    val success: Boolean,
    val error: String? = null
)
