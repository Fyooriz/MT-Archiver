# Keep cloud API
-keep class dev.mtarchiver.core.cloud.api.** { *; }
-keep interface dev.mtarchiver.core.cloud.api.** { *; }
-keepclassmembers class dev.mtarchiver.core.cloud.api.** { *; }

# Keep cloud providers
-keep class * implements dev.mtarchiver.core.cloud.api.CloudProviderProvider { *; }
-keepclassmembers class * implements dev.mtarchiver.core.cloud.api.CloudProviderProvider { *; }
