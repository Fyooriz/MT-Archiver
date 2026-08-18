package dev.mtarchiver.core.cloud.api

import java.util.*

/**
 * ServiceLoader SPI for cloud provider plugins.
 */
interface CloudProviderProvider {
    fun provideProvider(): CloudProvider

    companion object {
        fun loadAll(): List<CloudProvider> {
            return ServiceLoader.load(CloudProviderProvider::class.java)
                .mapNotNull { it.provideProvider() }
        }
    }
}
