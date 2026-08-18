package dev.mtarchiver.core.archive.impl

import android.content.Context
import dev.mtarchiver.core.archive.api.*
import dev.mtarchiver.core.archive.plugin.ArchiveFormatProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File

@Singleton
class ArchiveManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ArchiveManager {

    private val formats = mutableMapOf<String, ArchiveFormat>()
    private val formatsMutex = Mutex()
    private val operationsFlow = MutableSharedFlow<ArchiveOperation>(replay = 0)

    init {
        loadFormats()
    }

    private fun loadFormats() {
        // Load via ServiceLoader
        ArchiveFormatProvider.loadAll().forEach { format ->
            formats[format.name.lowercase()] = format
        }
    }

    override fun getFormats(): List<ArchiveFormat> {
        return formats.values.toList()
    }

    override fun getFormatByExtension(extension: String): ArchiveFormat? {
        val ext = extension.removePrefix(".").lowercase()
        return formats.values.firstOrNull { ext in it.extensions }
    }

    override fun getFormatForFile(file: File): ArchiveFormat? {
        return formats.values.firstOrNull { it.canHandle(file) }
    }

    override suspend fun listEntries(file: File): List<ArchiveEntry> {
        val format = getFormatForFile(file) ?: return emptyList()
        return format.listEntries(file)
    }

    override suspend fun extract(
        archiveFile: File,
        destinationDir: File,
        password: String?,
        progressListener: ProgressListener?
    ): ExtractResult {
        val format = getFormatForFile(archiveFile)
            ?: return ExtractResult(false, 0, 1, listOf("Format not supported"))

        return try {
            operationsFlow.emit(ArchiveOperation.Extracting(archiveFile.name, 0f))
            val result = format.extract(archiveFile, destinationDir, password, progressListener)
            operationsFlow.emit(ArchiveOperation.Completed("extract", result.success))
            result
        } catch (e: Exception) {
            ExtractResult(false, 0, 1, listOf(e.message ?: "Unknown error"))
        }
    }

    override suspend fun create(
        sourceFiles: List<File>,
        archiveFile: File,
        format: String,
        compressionLevel: Int,
        password: String?,
        progressListener: ProgressListener?
    ): CreateResult {
        val archiveFormat = getFormatByExtension(archiveFile.extension)
            ?: formats[format.lowercase()]
            ?: return CreateResult(false, 0, listOf("Format not supported: $format"))

        return try {
            operationsFlow.emit(ArchiveOperation.Creating(archiveFile.name, 0f))
            val result = archiveFormat.create(
                sourceFiles, archiveFile, compressionLevel, password, progressListener
            )
            operationsFlow.emit(ArchiveOperation.Completed("create", result.success))
            result
        } catch (e: Exception) {
            CreateResult(false, 0, listOf(e.message ?: "Unknown error"))
        }
    }

    override suspend fun test(archiveFile: File): TestResult {
        val format = getFormatForFile(archiveFile)
            ?: return TestResult(false, listOf("Format not supported"))

        return try {
            format.test(archiveFile)
        } catch (e: Exception) {
            TestResult(false, listOf(e.message ?: "Unknown error"))
        }
    }

    override fun registerFormat(format: ArchiveFormat) {
        formats[format.name.lowercase()] = format
    }

    override fun unregisterFormat(formatName: String) {
        formats.remove(formatName.lowercase())
    }

    override fun observeOperations(): SharedFlow<ArchiveOperation> {
        return operationsFlow
    }
}
