package dev.mtarchiver.core.cloud.provider.googledrive

import dev.mtarchiver.core.cloud.api.CloudProvider
import dev.mtarchiver.core.cloud.api.CloudProviderProvider

class GoogleDriveProviderProvider : CloudProviderProvider {
    override fun provideProvider(): CloudProvider = GoogleDriveProvider()
}
