# Keep archive API
-keep class dev.mtarchiver.core.archive.api.** { *; }
-keep interface dev.mtarchiver.core.archive.api.** { *; }
-keepclassmembers class dev.mtarchiver.core.archive.api.** { *; }

# Keep plugin providers
-keep class * implements dev.mtarchiver.core.archive.plugin.ArchiveFormatProvider { *; }
-keepclassmembers class * implements dev.mtarchiver.core.archive.plugin.ArchiveFormatProvider { *; }

# Apache Commons Compress
-keep class org.apache.commons.compress.** { *; }
-keep interface org.apache.commons.compress.** { *; }

# Zip4j
-keep class net.lingala.zip4j.** { *; }
-keep interface net.lingala.zip4j.** { *; }

# Junrar
-keep class com.github.junrar.** { *; }
-keep interface com.github.junrar.** { *; }

# Zstandard
-keep class com.facebook.zstd.** { *; }
