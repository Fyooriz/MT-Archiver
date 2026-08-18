package dev.mtarchiver.core.archive.plugin

import dev.mtarchiver.core.archive.api.ArchiveFormat
import java.util.*

/**
 * ServiceLoader SPI for archive format plugins.
 * Implement this interface in plugin modules.
 */
interface ArchiveFormatProvider {
    fun provideFormat(): ArchiveFormat

    companion object {
        fun loadAll(): List<ArchiveFormat> {
            return ServiceLoader.load(ArchiveFormatProvider::class.java)
                .mapNotNull { it.provideFormat() }
        }
    }
}
