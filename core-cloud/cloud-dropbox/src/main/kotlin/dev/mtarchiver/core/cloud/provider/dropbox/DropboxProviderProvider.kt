package dev.mtarchiver.core.cloud.provider.dropbox

import dev.mtarchiver.core.cloud.api.CloudProvider
import dev.mtarchiver.core.cloud.api.CloudProviderProvider

class DropboxProviderProvider : CloudProviderProvider {
    override fun provideProvider(): CloudProvider = DropboxProvider()
}
