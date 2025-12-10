package com.example.medicinereminder.domain

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.medicinereminder.R // You'll need an icon in res/drawable

class DoseAlarmWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    companion object {
        const val KEY_MEDICINE_ID = "medicine_id"
        const val KEY_SCHEDULE_TIME = "schedule_time"
        const val NOTIFICATION_CHANNEL_ID = "medicine_reminder_channel"
    }

    override fun doWork(): Result {
        val medicineId = inputData.getLong(KEY_MEDICINE_ID, 0)
        val scheduleTime = inputData.getString(KEY_SCHEDULE_TIME) ?: return Result.failure()

        // --- Logic to look up medicine details from DB (not shown for brevity) ---
        // In a real app, you'd query the DB here to get the Medicine name and dosage
        val medicineName = "Pill X"
        val dosage = "5mg"
        // The actual due time for logging history is the current time or calculated near it
        val actualDueTime = System.currentTimeMillis()

        showNotification(
            medicineId,
            medicineName,
            dosage,
            scheduleTime,
            actualDueTime
        )

        return Result.success()
    }

    private fun showNotification(
        id: Long,
        name: String,
        dosage: String,
        scheduleTime: String,
        dueTime: Long
    ) {
        val context = applicationContext
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Base intent to open the app (MainActivity)
        // You may need to create a notification channel once in your Application class or MainActivity

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Time to take your medicine!")
            .setContentText("$name ($dosage) is due at $scheduleTime.")
            .setSmallIcon(R.drawable.ic_pill) // Use a proper icon
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .addAction(createNotificationAction(context, id, dueTime, true)) // Taken button
            .addAction(createNotificationAction(context, id, dueTime, false)) // Missed button
            .build()

        notificationManager.notify(id.toInt(), notification) // Use medicine ID as notification ID
    }

    // Creates the PendingIntent for the notification action buttons
    private fun createNotificationAction(
        context: Context,
        medicineId: Long,
        dueTime: Long,
        isTakenAction: Boolean
    ): NotificationCompat.Action {
        val actionText = if (isTakenAction) "Taken" else "Missed"
        val actionValue = if (isTakenAction) "ACTION_TAKEN" else "ACTION_MISSED"

        // Use NotificationReceiver to handle button click (Lab 3)
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            this.action = actionValue // Distinguish between taken/missed
            putExtra("MEDICINE_ID", medicineId)
            putExtra("DUE_TIME", dueTime)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            (medicineId + (if(isTakenAction) 1000 else 2000)).toInt(), // Unique request code
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Action.Builder(
            0, // Icon is usually 0 for text-only buttons
            actionText,
            pendingIntent
        ).build()
    }
}