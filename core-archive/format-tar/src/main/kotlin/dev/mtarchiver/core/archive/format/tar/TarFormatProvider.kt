package dev.mtarchiver.core.archive.format.tar

import dev.mtarchiver.core.archive.api.ArchiveFormat
import dev.mtarchiver.core.archive.plugin.ArchiveFormatProvider

class TarFormatProvider : ArchiveFormatProvider {
    override fun provideFormat(): ArchiveFormat = TarFormat()
}
