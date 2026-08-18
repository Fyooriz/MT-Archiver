# Keep AI API
-keep class dev.mtarchiver.core.ai.api.** { *; }
-keep interface dev.mtarchiver.core.ai.api.** { *; }
-keepclassmembers class dev.mtarchiver.core.ai.api.** { *; }

# Keep data classes
-keep class dev.mtarchiver.core.ai.api.CodeSuggestion { *; }
-keep class dev.mtarchiver.core.ai.api.FileClassification { *; }
-keep class dev.mtarchiver.core.ai.api.AnomalyDetectionResult { *; }
-keep class dev.mtarchiver.core.ai.api.AIResponse { *; }

# TensorFlow Lite
-keep class org.tensorflow.lite.** { *; }
-keep interface org.tensorflow.lite.** { *; }
-dontwarn org.tensorflow.lite.**

# ONNX Runtime
-keep class com.microsoft.onnxruntime.** { *; }
-keep interface com.microsoft.onnxruntime.** { *; }
-dontwarn com.microsoft.onnxruntime.**
