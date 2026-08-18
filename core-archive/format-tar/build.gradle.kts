plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace = "dev.mtarchiver.core.archive.format.tar"
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
    implementation("org.apache.commons:commons-compress:1.24.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation(project(":core-archive"))
    implementation(project(":core-common"))
}
