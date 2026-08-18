# Keep security API
-keep class dev.mtarchiver.core.security.api.** { *; }
-keep interface dev.mtarchiver.core.security.api.** { *; }
-keepclassmembers class dev.mtarchiver.core.security.api.** { *; }

# Keep data classes
-keep class dev.mtarchiver.core.security.api.Credential { *; }

# BouncyCastle
-keep class org.bouncycastle.** { *; }
-keep interface org.bouncycastle.** { *; }
-dontwarn org.bouncycastle.**

# Tink
-keep class com.google.crypto.tink.** { *; }
-keep interface com.google.crypto.tink.** { *; }
-dontwarn com.google.crypto.tink.**
