package dev.mtarchiver.core.ai.api

import java.io.File

/**
 * AI Service for integrated AI features across the app.
 */
interface AIService {
    /**
     * Get code completion suggestions.
     */
    suspend fun getCodeCompletion(
        code: String,
        language: String,
        position: Int
    ): List<CodeSuggestion>

    /**
     * Analyze file and suggest compression format.
     */
    suspend fun suggestCompressionFormat(file: File): Map<String, Float>

    /**
     * Classify and auto-organize files.
     */
    suspend fun classifyFile(file: File): FileClassification

    /**
     * Detect anomalies/security threats in files.
     */
    suspend fun detectAnomalies(file: File): AnomalyDetectionResult

    /**
     * Chat-based archive management.
     */
    suspend fun chat(message: String): AIResponse

    /**
     * Search files semantically.
     */
    suspend fun semanticSearch(query: String, files: List<File>): List<File>

    /**
     * Generate text using LLM.
     */
    suspend fun generateText(prompt: String, maxTokens: Int = 512): String

    /**
     * Check if local models are available.
     */
    fun hasLocalModels(): Boolean
}

data class AnomalyDetectionResult(
    val isAnomalous: Boolean,
    val confidence: Float,
    val threats: List<String> = emptyList(),
    val recommendations: List<String> = emptyList()
)
