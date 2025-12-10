plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21"

    // 💡 ADDED: Hilt Plugin
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.medicinereminder"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.medicinereminder"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        // 💡 MODIFIED: Use the modern standard for new projects
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        // 💡 MODIFIED: Match Java version
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        // 💡 MODIFIED: Set to the latest version compatible with Kotlin 2.0.21
        kotlinCompilerExtensionVersion = "2.0.0"
    }
}

dependencies {
    // Core
    // (Updated dependencies to more recent stable versions for a 2025 project)
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")

    // Compose
    implementation(platform("androidx.compose:compose-bom:2024.05.00")) // 💡 UPDATED BOM
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // ViewModel + Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // 🔑 ADDED FIX: Hilt Navigation Compose (Required for hiltViewModel())
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Room
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    // WorkManager
    val workVersion = "2.9.0"
    implementation("androidx.work:work-runtime-ktx:$workVersion")

    // 🧠 HILT (Dependency Injection)
    val hiltVersion = "2.48"
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")

    // 🔔 HILT for WorkManager (Crucial for your notifications)
    val hiltWorkVersion = "1.2.0"
    implementation("androidx.hilt:hilt-work:$hiltWorkVersion")
    kapt("androidx.hilt:hilt-compiler:$hiltWorkVersion")

    // Testing
    testImplementation("junit:junit:4.13.2")
    // ...
}