plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "dev.mtarchiver.core.editor"
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
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    
    // Code editor
    implementation("io.github.rosemoe.sora-editor:editor:0.23.2")
    implementation("io.github.rosemoe.sora-editor:language-textmate:0.23.2")
    
    // Git integration
    implementation("org.eclipse.jgit:org.eclipse.jgit:6.7.0.202309050840-r")
    
    // LSP support
    implementation("org.eclipse.lsp4j:org.eclipse.lsp4j:0.21.1")
    
    implementation(project(":core-common"))
    testImplementation("junit:junit:4.13.2")
}
