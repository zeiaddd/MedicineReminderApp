package com.example.medicinereminder

import android.app.Application
import dagger.hilt.android.HiltAndroidApp // Import Hilt annotation

/**
 * 🔑 Hilt Application Entry Point
 * The @HiltAndroidApp annotation triggers Hilt's code generation
 * and sets up the dependency graph for the entire application.
 */
@HiltAndroidApp
class MedicineReminderApplication : Application() {
    // No code needed inside the class body itself for this project,
    // as its primary job is to serve as the Hilt entry point.
}