package dev.mtarchiver.core.enterprise.api

/**
 * Enterprise authentication and authorization interfaces.
 */
interface EnterpriseAuth {
    /**
     * Authenticate with SSO (SAML, OAuth, OpenID Connect).
     */
    suspend fun authenticateSSO(provider: String, credentials: Map<String, String>): AuthResult

    /**
     * Authenticate with LDAP.
     */
    suspend fun authenticateLDAP(server: String, username: String, password: String): AuthResult

    /**
     * Authenticate with Kerberos.
     */
    suspend fun authenticateKerberos(principal: String): AuthResult

    /**
     * Get current user info.
     */
    suspend fun getCurrentUser(): UserInfo?

    /**
     * Logout.
     */
    suspend fun logout(): Boolean
}

data class AuthResult(
    val success: Boolean,
    val token: String? = null,
    val refreshToken: String? = null,
    val expiresIn: Long = 0,
    val error: String? = null
)

data class UserInfo(
    val id: String,
    val username: String,
    val email: String,
    val displayName: String,
    val groups: List<String> = emptyList(),
    val roles: List<String> = emptyList()
)

/**
 * Role-based access control (RBAC).
 */
interface RBACManager {
    /**
     * Check if user has permission.
     */
    suspend fun hasPermission(userId: String, permission: String): Boolean

    /**
     * Check if user has role.
     */
    suspend fun hasRole(userId: String, role: String): Boolean

    /**
     * Get user permissions.
     */
    suspend fun getUserPermissions(userId: String): List<String>

    /**
     * Grant permission to user.
     */
    suspend fun grantPermission(userId: String, permission: String): Boolean

    /**
     * Revoke permission from user.
     */
    suspend fun revokePermission(userId: String, permission: String): Boolean
}

/**
 * Audit trail for compliance.
 */
interface AuditManager {
    /**
     * Log audit event.
     */
    suspend fun logEvent(event: AuditEvent): Boolean

    /**
     * Get audit logs.
     */
    suspend fun getLogs(userId: String? = null, action: String? = null, limit: Int = 100): List<AuditEvent>

    /**
     * Export audit logs.
     */
    suspend fun exportLogs(format: String = "csv"): ByteArray
}

data class AuditEvent(
    val id: String = "",
    val userId: String,
    val action: String,
    val resource: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String,
    val details: Map<String, Any> = emptyMap(),
    val ipAddress: String? = null
)

/**
 * Mobile Device Management (MDM).
 */
interface MDMManager {
    /**
     * Get device policies.
     */
    suspend fun getDevicePolicies(): DevicePolicies

    /**
     * Apply device lock.
     */
    suspend fun lockDevice(): Boolean

    /**
     * Remote wipe device.
     */
    suspend fun wipeDevice(): Boolean

    /**
     * Get device info.
     */
    suspend fun getDeviceInfo(): DeviceInfo
}

data class DevicePolicies(
    val passwordRequired: Boolean = true,
    val minPasswordLength: Int = 8,
    val encryptionRequired: Boolean = true,
    val biometricEnabled: Boolean = true,
    val kioskMode: Boolean = false,
    val allowedApps: List<String> = emptyList(),
    val restrictions: Map<String, Boolean> = emptyMap()
)

data class DeviceInfo(
    val deviceId: String,
    val manufacturer: String,
    val model: String,
    val osVersion: String,
    val lastCheckIn: Long,
    val isCompliant: Boolean
)
