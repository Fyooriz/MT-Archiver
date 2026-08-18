package dev.mtarchiver.core.network.impl

import dev.mtarchiver.core.network.api.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class DownloadManagerImpl @Inject constructor() : DownloadManager {

    private val downloads = mutableMapOf<String, DownloadStatus>()

    override suspend fun addDownload(url: String, outputPath: String, headers: Map<String, String>): String {
        return withContext(Dispatchers.IO) {
            val downloadId = "download_${System.currentTimeMillis()}"
            val fileName = url.substringAfterLast("/")
            downloads[downloadId] = DownloadStatus(
                id = downloadId,
                url = url,
                fileName = fileName,
                totalSize = 0,
                downloadedSize = 0,
                progress = 0f,
                status = DownloadStatusEnum.PENDING,
                speed = 0,
                eta = 0
            )
            downloadId
        }
    }

    override suspend fun pauseDownload(downloadId: String): Boolean {
        return withContext(Dispatchers.Default) {
            downloads[downloadId]?.let {
                downloads[downloadId] = it.copy(status = DownloadStatusEnum.PAUSED)
                true
            } ?: false
        }
    }

    override suspend fun resumeDownload(downloadId: String): Boolean {
        return withContext(Dispatchers.Default) {
            downloads[downloadId]?.let {
                downloads[downloadId] = it.copy(status = DownloadStatusEnum.DOWNLOADING)
                true
            } ?: false
        }
    }

    override suspend fun cancelDownload(downloadId: String): Boolean {
        return withContext(Dispatchers.Default) {
            downloads.remove(downloadId) != null
        }
    }

    override suspend fun getStatus(downloadId: String): DownloadStatus? {
        return withContext(Dispatchers.Default) {
            downloads[downloadId]
        }
    }

    override suspend fun getAllDownloads(): List<DownloadStatus> {
        return withContext(Dispatchers.Default) {
            downloads.values.toList()
        }
    }
}

@Singleton
class VPNManagerImpl @Inject constructor() : VPNManager {

    private var currentStatus = VPNStatus(isConnected = false)

    override suspend fun getAvailableProviders(): List<VPNProvider> {
        return withContext(Dispatchers.Default) {
            listOf(
                VPNProvider(
                    id = "openvpn_1",
                    name = "OpenVPN",
                    type = "OpenVPN",
                    servers = listOf(
                        VPNServer("us-1", "US Server 1", "USA", "192.168.1.1", 1194),
                        VPNServer("eu-1", "EU Server 1", "Germany", "192.168.1.2", 1194)
                    )
                ),
                VPNProvider(
                    id = "wireguard_1",
                    name = "WireGuard",
                    type = "WireGuard"
                )
            )
        }
    }

    override suspend fun connect(providerId: String, credentials: Map<String, String>): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                currentStatus = VPNStatus(
                    isConnected = true,
                    provider = providerId,
                    ip = "203.0.113.${(1..254).random()}"
                )
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    override suspend fun disconnect(): Boolean {
        return withContext(Dispatchers.IO) {
            currentStatus = VPNStatus(isConnected = false)
            true
        }
    }

    override suspend fun getStatus(): VPNStatus {
        return withContext(Dispatchers.Default) {
            currentStatus
        }
    }

    override suspend fun setConfiguration(config: VPNConfig): Boolean {
        return withContext(Dispatchers.Default) {
            true
        }
    }
}
