package dev.mtarchiver.core.archive.format.mta

import dev.mtarchiver.core.archive.api.ArchiveFormat
import dev.mtarchiver.core.archive.plugin.ArchiveFormatProvider

class MtaFormatProvider : ArchiveFormatProvider {
    override fun provideFormat(): ArchiveFormat = MtaFormat()
}
