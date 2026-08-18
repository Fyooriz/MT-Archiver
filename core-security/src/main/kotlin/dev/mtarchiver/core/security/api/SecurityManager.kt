package dev.mtarchiver.core.security.api

import java.io.File

/**
 * Security and encryption interface.
 */
interface SecurityManager {
    /**
     * Encrypt file with AES-256.
     */
    suspend fun encryptFile(inputFile: File, outputFile: File, password: String): Boolean

    /**
     * Decrypt file.
     */
    suspend fun decryptFile(inputFile: File, outputFile: File, password: String): Boolean

    /**
     * Generate secure password.
     */
    suspend fun generatePassword(length: Int = 16): String

    /**
     * Hash password.
     */
    suspend fun hashPassword(password: String): String

    /**
     * Verify password hash.
     */
    suspend fun verifyPassword(password: String, hash: String): Boolean

    /**
     * Calculate file hash.
     */
    suspend fun calculateFileHash(file: File): String

    /**
     * Check file integrity.
     */
    suspend fun verifyFileHash(file: File, expectedHash: String): Boolean
}

/**
 * Biometric authentication interface.
 */
interface BiometricAuth {
    /**
     * Check if biometric is available.
     */
    fun isBiometricAvailable(): Boolean

    /**
     * Authenticate with biometric.
     */
    suspend fun authenticate(): Boolean

    /**
     * Enable biometric lock.
     */
    suspend fun enableBiometric(): Boolean

    /**
     * Disable biometric lock.
     */
    suspend fun disableBiometric(): Boolean
}

/**
 * Password manager interface.
 */
interface PasswordManager {
    /**
     * Store password securely.
     */
    suspend fun storePassword(service: String, username: String, password: String): Boolean

    /**
     * Retrieve stored password.
     */
    suspend fun getPassword(service: String, username: String): String?

    /**
     * Delete stored password.
     */
    suspend fun deletePassword(service: String, username: String): Boolean

    /**
     * List all stored credentials.
     */
    suspend fun listCredentials(): List<Credential>
}

data class Credential(
    val service: String,
    val username: String,
    val lastUpdated: Long
)
