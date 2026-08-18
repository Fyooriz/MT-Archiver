package dev.mtarchiver.core.cloud.provider.sftp

import dev.mtarchiver.core.cloud.api.*
import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.JSch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class SftpProvider : CloudProvider {
    override val name = "SFTP"
    override val icon = "ic_sftp"

    private var channel: ChannelSftp? = null
    private val jsch = JSch()

    override suspend fun authenticate(credentials: AuthCredentials): AuthResult {
        return withContext(Dispatchers.IO) {
            try {
                val session = jsch.getSession(
                    credentials.username,
                    credentials.url,
                    credentials.port ?: 22
                )
                session.setPassword(credentials.password)
                session.setConfig("StrictHostKeyChecking", "no")
                session.connect()

                val chan = session.openChannel("sftp") as ChannelSftp
                chan.connect()
                channel = chan

                AuthResult(true, accessToken = "connected")
            } catch (e: Exception) {
                AuthResult(false, error = e.message)
            }
        }
    }

    override suspend fun isAuthenticated(): Boolean {
        return channel?.isConnected == true
    }

    override suspend fun logout() {
        withContext(Dispatchers.IO) {
            channel?.disconnect()
            channel = null
        }
    }

    override suspend fun listFiles(path: String): List<CloudFile> {
        return withContext(Dispatchers.IO) {
            try {
                val files = mutableListOf<CloudFile>()
                channel?.ls(path)?.forEach { entry ->
                    if (entry is ChannelSftp.LsEntry && entry.filename != "." && entry.filename != "..") {
                        files.add(
                            CloudFile(
                                name = entry.filename,
                                path = "$path/${entry.filename}",
                                isDirectory = entry.attrs.isDir,
                                size = entry.attrs.size,
                                lastModified = entry.attrs.mtime.toLong() * 1000
                            )
                        )
                    }
                }
                files
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    override suspend fun downloadFile(remotePath: String, localFile: File): DownloadResult {
        return withContext(Dispatchers.IO) {
            try {
                channel?.get(remotePath, localFile.absolutePath)
                DownloadResult(true, downloadedSize = localFile.length())
            } catch (e: Exception) {
                DownloadResult(false, error = e.message)
            }
        }
    }

    override suspend fun uploadFile(localFile: File, remotePath: String): UploadResult {
        return withContext(Dispatchers.IO) {
            try {
                channel?.put(localFile.absolutePath, remotePath)
                UploadResult(true, uploadedSize = localFile.length())
            } catch (e: Exception) {
                UploadResult(false, error = e.message)
            }
        }
    }

    override suspend fun delete(path: String): DeleteResult {
        return withContext(Dispatchers.IO) {
            try {
                channel?.rm(path)
                DeleteResult(true)
            } catch (e: Exception) {
                DeleteResult(false, error = e.message)
            }
        }
    }

    override suspend fun createFolder(path: String): CreateFolderResult {
        return withContext(Dispatchers.IO) {
            try {
                channel?.mkdir(path)
                CreateFolderResult(true)
            } catch (e: Exception) {
                CreateFolderResult(false, error = e.message)
            }
        }
    }

    override suspend fun rename(path: String, newName: String): RenameResult {
        return withContext(Dispatchers.IO) {
            try {
                channel?.rename(path, newName)
                RenameResult(true)
            } catch (e: Exception) {
                RenameResult(false, error = e.message)
            }
        }
    }
}
