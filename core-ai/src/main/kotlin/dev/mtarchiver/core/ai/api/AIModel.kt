package dev.mtarchiver.core.ai.api

/**
 * AI Model interface for on-device inference.
 */
interface AIModel {
    val name: String
    val version: String
    val type: ModelType
    val inputShape: IntArray
    val outputShape: IntArray

    /**
     * Load model into memory.
     */
    suspend fun load(modelPath: String): Boolean

    /**
     * Unload model from memory.
     */
    suspend fun unload()

    /**
     * Run inference on input.
     */
    suspend fun infer(input: FloatArray): FloatArray

    /**
     * Run inference with string input (for NLP models).
     */
    suspend fun inferText(text: String): Map<String, Float>
}

enum class ModelType {
    TEXT_CLASSIFICATION,
    IMAGE_CLASSIFICATION,
    OBJECT_DETECTION,
    TEXT_GENERATION,
    CODE_COMPLETION,
    EMBEDDING,
    SEMANTIC_SEARCH
}

data class AIResponse(
    val text: String,
    val confidence: Float,
    val metadata: Map<String, Any> = emptyMap()
)

data class CodeSuggestion(
    val code: String,
    val language: String,
    val confidence: Float,
    val description: String? = null
)

data class FileClassification(
    val category: String,
    val confidence: Float,
    val tags: List<String> = emptyList()
)
