plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "dev.mtarchiver.core.archive"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
        targetSdk = 34
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // Androidx
    implementation("androidx.core:core-ktx:1.12.0")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    // Archive libraries
    implementation("org.apache.commons:commons-compress:1.24.0")
    implementation("net.lingala.zip4j:zip4j:2.11.5")
    implementation("com.github.junrar:junrar:7.5.5")
    implementation("com.facebook.zstd:zstd-jni:1.5.5-5")

    // Local modules
    implementation(project(":core-common"))

    // Testing
    testImplementation("junit:junit:4.13.2")
}
