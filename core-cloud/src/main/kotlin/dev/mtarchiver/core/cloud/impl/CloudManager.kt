package dev.mtarchiver.core.cloud.impl

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import dev.mtarchiver.core.cloud.api.AuthCredentials
import dev.mtarchiver.core.cloud.api.AuthResult
import dev.mtarchiver.core.cloud.api.CloudProvider
import dev.mtarchiver.core.cloud.api.CloudProviderProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@Singleton
class CloudManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val encryptedPrefs: EncryptedSharedPreferences
) {
    private val providers = mutableMapOf<String, CloudProvider>()
    private val eventFlow = MutableSharedFlow<CloudEvent>(replay = 0)

    init {
        loadProviders()
    }

    private fun loadProviders() {
        CloudProviderProvider.loadAll().forEach { provider ->
            providers[provider.name.lowercase()] = provider
        }
    }

    fun getProviders(): List<CloudProvider> {
        return providers.values.toList()
    }

    fun getProvider(name: String): CloudProvider? {
        return providers[name.lowercase()]
    }

    fun registerProvider(provider: CloudProvider) {
        providers[provider.name.lowercase()] = provider
    }

    fun unregisterProvider(name: String) {
        providers.remove(name.lowercase())
    }

    fun observeEvents(): SharedFlow<CloudEvent> {
        return eventFlow
    }

    suspend fun emitEvent(event: CloudEvent) {
        eventFlow.emit(event)
    }

    fun saveCredentials(providerName: String, credentials: AuthCredentials) {
        encryptedPrefs.edit().apply {
            putString("${providerName}_token", credentials.token)
            putString("${providerName}_refresh_token", credentials.refreshToken)
            putString("${providerName}_username", credentials.username)
            apply()
        }
    }

    fun getCredentials(providerName: String): AuthCredentials? {
        val token = encryptedPrefs.getString("${providerName}_token", null)
        val refreshToken = encryptedPrefs.getString("${providerName}_refresh_token", null)
        val username = encryptedPrefs.getString("${providerName}_username", null)

        return if (token != null) {
            AuthCredentials(
                token = token,
                refreshToken = refreshToken,
                username = username
            )
        } else {
            null
        }
    }

    fun clearCredentials(providerName: String) {
        encryptedPrefs.edit().apply {
            remove("${providerName}_token")
            remove("${providerName}_refresh_token")
            remove("${providerName}_username")
            apply()
        }
    }
}

sealed class CloudEvent {
    data class Authenticated(val provider: String) : CloudEvent()
    data class Logout(val provider: String) : CloudEvent()
    data class FileDownloaded(val provider: String, val path: String) : CloudEvent()
    data class FileUploaded(val provider: String, val path: String) : CloudEvent()
    data class Error(val provider: String, val message: String) : CloudEvent()
}
