pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MedicineReminderApp"
include(":app")

// 💡 CORRECTED BLOCK FOR FLUTTER INTEGRATION
// This block is necessary to evaluate the flutter module's internal configuration.
val flutterProjectRoot = settingsDir.parentFile
if (flutterProjectRoot.name == "app") {
    // Handle the case where the Android project is nested inside the Flutter project
    apply(from = "$flutterProjectRoot/../flutter_module/.android/include_flutter.groovy")
} else {
    // Standard setup: Flutter module next to Android project
    val flutterIncludeFile = File(flutterProjectRoot, "flutter_module/.android/include_flutter.groovy")
    if (flutterIncludeFile.exists()) {
        apply(from = flutterIncludeFile)
    }
}