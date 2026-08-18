package dev.mtarchiver.core.archive.impl.zip

import dev.mtarchiver.core.archive.api.ArchiveFormat
import dev.mtarchiver.core.archive.plugin.ArchiveFormatProvider

class ZipFormatProvider : ArchiveFormatProvider {
    override fun provideFormat(): ArchiveFormat = ZipFormat()
}
