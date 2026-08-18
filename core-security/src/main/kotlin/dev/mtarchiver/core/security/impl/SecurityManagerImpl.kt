package dev.mtarchiver.core.security.impl

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import dev.mtarchiver.core.security.api.SecurityManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec

@Singleton
class SecurityManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val encryptedPrefs: EncryptedSharedPreferences
) : SecurityManager {

    override suspend fun encryptFile(inputFile: File, outputFile: File, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val cipher = Cipher.getInstance("AES")
                val key = deriveKey(password)
                cipher.init(Cipher.ENCRYPT_MODE, key)

                val inputBytes = inputFile.readBytes()
                val encryptedBytes = cipher.doFinal(inputBytes)
                outputFile.writeBytes(encryptedBytes)
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    override suspend fun decryptFile(inputFile: File, outputFile: File, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val cipher = Cipher.getInstance("AES")
                val key = deriveKey(password)
                cipher.init(Cipher.DECRYPT_MODE, key)

                val encryptedBytes = inputFile.readBytes()
                val decryptedBytes = cipher.doFinal(encryptedBytes)
                outputFile.writeBytes(decryptedBytes)
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    override suspend fun generatePassword(length: Int): String {
        return withContext(Dispatchers.Default) {
            val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()"
            val random = SecureRandom()
            (1..length).map { chars[random.nextInt(chars.length)] }.joinToString("")
        }
    }

    override suspend fun hashPassword(password: String): String {
        return withContext(Dispatchers.Default) {
            val md = MessageDigest.getInstance("SHA-256")
            md.digest(password.toByteArray()).joinToString("") { "%02x".format(it) }
        }
    }

    override suspend fun verifyPassword(password: String, hash: String): Boolean {
        return withContext(Dispatchers.Default) {
            hashPassword(password) == hash
        }
    }

    override suspend fun calculateFileHash(file: File): String {
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

    override suspend fun verifyFileHash(file: File, expectedHash: String): Boolean {
        return withContext(Dispatchers.IO) {
            calculateFileHash(file) == expectedHash
        }
    }

    private fun deriveKey(password: String): SecretKeySpec {
        val md = MessageDigest.getInstance("SHA-256")
        val keyBytes = md.digest(password.toByteArray())
        return SecretKeySpec(keyBytes, 0, 32, "AES")
    }
}
