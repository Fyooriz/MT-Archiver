package dev.mtarchiver.core.network.api

/**
 * Download manager interface.
 */
interface DownloadManager {
    /**
     * Add download task.
     */
    suspend fun addDownload(url: String, outputPath: String, headers: Map<String, String> = emptyMap()): String

    /**
     * Pause download.
     */
    suspend fun pauseDownload(downloadId: String): Boolean

    /**
     * Resume download.
     */
    suspend fun resumeDownload(downloadId: String): Boolean

    /**
     * Cancel download.
     */
    suspend fun cancelDownload(downloadId: String): Boolean

    /**
     * Get download status.
     */
    suspend fun getStatus(downloadId: String): DownloadStatus?

    /**
     * Get all downloads.
     */
    suspend fun getAllDownloads(): List<DownloadStatus>
}

data class DownloadStatus(
    val id: String,
    val url: String,
    val fileName: String,
    val totalSize: Long,
    val downloadedSize: Long,
    val progress: Float,
    val status: DownloadStatusEnum,
    val speed: Long, // bytes per second
    val eta: Long // seconds
)

enum class DownloadStatusEnum {
    PENDING, DOWNLOADING, PAUSED, COMPLETED, FAILED, CANCELLED
}

/**
 * VPN interface.
 */
interface VPNManager {
    /**
     * Get available VPN providers.
     */
    suspend fun getAvailableProviders(): List<VPNProvider>

    /**
     * Connect to VPN.
     */
    suspend fun connect(providerId: String, credentials: Map<String, String>): Boolean

    /**
     * Disconnect VPN.
     */
    suspend fun disconnect(): Boolean

    /**
     * Get current VPN status.
     */
    suspend fun getStatus(): VPNStatus

    /**
     * Set VPN configuration.
     */
    suspend fun setConfiguration(config: VPNConfig): Boolean
}

data class VPNProvider(
    val id: String,
    val name: String,
    val type: String, // OpenVPN, WireGuard, etc
    val servers: List<VPNServer> = emptyList()
)

data class VPNServer(
    val id: String,
    val name: String,
    val country: String,
    val ip: String,
    val port: Int
)

data class VPNConfig(
    val protocol: String,
    val encryptionLevel: String,
    val killSwitch: Boolean = true,
    val splitTunneling: Boolean = false
)

data class VPNStatus(
    val isConnected: Boolean,
    val provider: String? = null,
    val ip: String? = null,
    val uploadSpeed: Long = 0,
    val downloadSpeed: Long = 0
)

/**
 * Network file server interface (FTP, SMB, etc).
 */
interface NetworkFileServer {
    /**
     * Start FTP server.
     */
    suspend fun startFTPServer(port: Int = 21, username: String, password: String): Boolean

    /**
     * Start SMB server.
     */
    suspend fun startSMBServer(port: Int = 445): Boolean

    /**
     * Stop server.
     */
    suspend fun stopServer(type: String): Boolean

    /**
     * Get server status.
     */
    suspend fun getServerStatus(type: String): ServerStatus
}

data class ServerStatus(
    val type: String,
    val isRunning: Boolean,
    val port: Int,
    val connectedClients: Int,
    val uploadedBytes: Long,
    val downloadedBytes: Long
)
