package com.example.medicinereminder.domain

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import dagger.hilt.android.EntryPointAccessors
import com.example.medicinereminder.data.repository.MedicineRepository
import com.example.medicinereminder.di.ReceiverEntryPoint // 🔑 Import the new interface

class NotificationReceiver : BroadcastReceiver() {

    // We use a property to hold the repository, initialized lazily
    private lateinit var repository: MedicineRepository

    override fun onReceive(context: Context, intent: Intent) {

        // 🔑 FIX: Retrieve the repository using the Hilt Entry Point
        // This replaces the manual (and incorrect) initialization.
        val hiltEntryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            ReceiverEntryPoint::class.java
        )
        repository = hiltEntryPoint.repository()

        val action = intent.action
        val medicineId = intent.getLongExtra("MEDICINE_ID", -1)
        val dueTime = intent.getLongExtra("DUE_TIME", -1)

        if (medicineId == -1L || dueTime == -1L) return

        val isTaken = action == "ACTION_TAKEN"

        // Use a CoroutineScope to perform database operation
        CoroutineScope(Dispatchers.IO).launch {

            // 1. Update the database history table using the injected repository
            repository.updateDoseHistory(medicineId, dueTime, isTaken)

            // 2. Dismiss the notification (optional)

            // Show a simple confirmation toast on the main thread
            CoroutineScope(Dispatchers.Main).launch {
                val status = if (isTaken) "Taken" else "Missed"
                Toast.makeText(context, "$status recorded for dose.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}