# Keep common classes
-keep class dev.mtarchiver.core.common.** { *; }
-keepclassmembers class dev.mtarchiver.core.common.** { *; }

# Coroutines
-keepclasseswithmembers class kotlinx.coroutines.internal.MainDispatcherFactory { *; }
-keepclasseswithmembers class kotlinx.coroutines.android.AndroidDispatcherFactory { *; }

# Retrofit
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }
-keepclasseswithmembers class * { @retrofit2.* <methods>; }

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
