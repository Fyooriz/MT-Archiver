package dev.mtarchiver.core.multimedia.api

import java.io.File

/**
 * Media player interface.
 */
interface MediaPlayer {
    /**
     * Load media file.
     */
    suspend fun loadMedia(file: File): Boolean

    /**
     * Play media.
     */
    suspend fun play(): Boolean

    /**
     * Pause media.
     */
    suspend fun pause(): Boolean

    /**
     * Stop media.
     */
    suspend fun stop(): Boolean

    /**
     * Seek to position.
     */
    suspend fun seekTo(positionMs: Long): Boolean

    /**
     * Get current playback info.
     */
    suspend fun getPlaybackInfo(): PlaybackInfo?

    /**
     * Get media metadata.
     */
    suspend fun getMediaMetadata(): MediaMetadata?
}

data class PlaybackInfo(
    val currentPositionMs: Long,
    val durationMs: Long,
    val isPlaying: Boolean,
    val volume: Float,
    val speed: Float
)

data class MediaMetadata(
    val title: String,
    val artist: String?,
    val album: String?,
    val duration: Long,
    val bitrate: Int,
    val resolution: String?,
    val frameRate: Float = 0f
)

/**
 * Image editor interface.
 */
interface ImageEditor {
    /**
     * Load image.
     */
    suspend fun loadImage(file: File): Boolean

    /**
     * Rotate image.
     */
    suspend fun rotate(degrees: Float): Boolean

    /**
     * Crop image.
     */
    suspend fun crop(x: Int, y: Int, width: Int, height: Int): Boolean

    /**
     * Resize image.
     */
    suspend fun resize(width: Int, height: Int): Boolean

    /**
     * Apply filter.
     */
    suspend fun applyFilter(filterName: String): Boolean

    /**
     * Adjust brightness.
     */
    suspend fun adjustBrightness(value: Float): Boolean

    /**
     * Adjust contrast.
     */
    suspend fun adjustContrast(value: Float): Boolean

    /**
     * Save edited image.
     */
    suspend fun save(outputFile: File, format: String = "png", quality: Int = 95): Boolean
}

/**
 * PDF viewer and editor interface.
 */
interface PDFManager {
    /**
     * Load PDF document.
     */
    suspend fun loadPDF(file: File): Boolean

    /**
     * Get total pages.
     */
    suspend fun getPageCount(): Int

    /**
     * Get page as bitmap.
     */
    suspend fun getPageBitmap(pageNumber: Int, width: Int, height: Int): ByteArray?

    /**
     * Extract text from PDF.
     */
    suspend fun extractText(pageNumber: Int): String

    /**
     * Search in PDF.
     */
    suspend fun search(query: String): List<SearchResult>

    /**
     * Add annotation.
     */
    suspend fun addAnnotation(pageNumber: Int, annotation: PDFAnnotation): Boolean

    /**
     * Save PDF.
     */
    suspend fun savePDF(outputFile: File): Boolean
}

data class SearchResult(
    val pageNumber: Int,
    val text: String,
    val position: Int
)

data class PDFAnnotation(
    val type: String, // highlight, note, underline
    val content: String,
    val x: Float,
    val y: Float,
    val color: Int = 0xFFFFFF
)
