package dev.mtarchiver.core.filemanager.impl

import android.content.Context
import dev.mtarchiver.core.filemanager.api.FileManager
import dev.mtarchiver.core.filemanager.api.FileInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.security.MessageDigest

@Singleton
class FileManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : FileManager {

    override suspend fun listFiles(directory: File): List<FileInfo> {
        return withContext(Dispatchers.IO) {
            try {
                directory.listFiles()?.map { file ->
                    FileInfo(
                        file = file,
                        name = file.name,
                        size = file.length(),
                        lastModified = file.lastModified(),
                        isDirectory = file.isDirectory,
                        permissions = "%o".format(file.canRead(), file.canWrite(), file.canExecute()),
                        isHidden = file.isHidden
                    )
                } ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    override suspend fun copyFile(source: File, destination: File): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                source.copyTo(destination, overwrite = true)
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    override suspend fun moveFile(source: File, destination: File): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                source.renameTo(destination)
            } catch (e: Exception) {
                false
            }
        }
    }

    override suspend fun secureDelete(file: File): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // DoD 5220.22-M: overwrite file 3 times
                val size = file.length().toInt()
                val randomData = ByteArray(size) { (Math.random() * 256).toByte() }

                repeat(3) {
                    file.writeBytes(randomData)
                }

                file.delete()
            } catch (e: Exception) {
                false
            }
        }
    }

    override suspend fun getDirectorySize(directory: File): Long {
        return withContext(Dispatchers.IO) {
            var size = 0L
            try {
                directory.walkTopDown().forEach { file ->
                    size += file.length()
                }
            } catch (e: Exception) {
                size = 0L
            }
            size
        }
    }

    override suspend fun searchFiles(directory: File, pattern: String): List<File> {
        return withContext(Dispatchers.IO) {
            try {
                directory.walkTopDown()
                    .filter { it.name.contains(pattern, ignoreCase = true) }
                    .toList()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    override suspend fun calculateHash(file: File): String {
        return withContext(Dispatchers.IO) {
            try {
                val md = MessageDigest.getInstance("SHA-256")
                val buffer = ByteArray(8192)
                file.inputStream().use { input ->
                    var read: Int
                    while (input.read(buffer).also { read = it } != -1) {
                        md.update(buffer, 0, read)
                    }
                }
                md.digest().joinToString("") { "%02x".format(it) }
            } catch (e: Exception) {
                ""
            }
        }
    }
}
