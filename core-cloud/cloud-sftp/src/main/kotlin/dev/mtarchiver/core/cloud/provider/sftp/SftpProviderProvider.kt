package dev.mtarchiver.core.cloud.provider.sftp

import dev.mtarchiver.core.cloud.api.CloudProvider
import dev.mtarchiver.core.cloud.api.CloudProviderProvider

class SftpProviderProvider : CloudProviderProvider {
    override fun provideProvider(): CloudProvider = SftpProvider()
}
