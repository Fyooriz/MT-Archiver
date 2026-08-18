plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace = "dev.mtarchiver.core.archive.format.zip"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
        targetSdk = 34
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
    implementation("net.lingala.zip4j:zip4j:2.11.5")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation(project(":core-archive"))
    implementation(project(":core-common"))
}
