package com.example.medicinereminder.util

import java.util.Calendar
import java.util.concurrent.TimeUnit

/**
 * Pure business logic for calculating dose times and delays.
 * This object is highly testable using JUnit (Lab 7).
 */
object ScheduleCalculator {

    /**
     * Calculates the initial delay in milliseconds from the current time
     * until the next occurrence of the given timeString (e.g., "08:00").
     * This delay is used to set the initial start time for WorkManager's PeriodicWorkRequest.
     * * @param timeString The scheduled time of day (e.g., "14:30").
     * @return The delay in milliseconds.
     */
    fun calculateInitialDelay(timeString: String): Long {
        val parts = timeString.split(":")
        val targetHour = parts[0].toInt()
        val targetMinute = parts[1].toInt()

        // 1. Get the current time
        val now = Calendar.getInstance()

        // 2. Set the target time for today
        val nextSchedule = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, targetHour)
            set(Calendar.MINUTE, targetMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // 3. If the scheduled time has already passed today, push it to tomorrow.
        if (nextSchedule.timeInMillis <= now.timeInMillis) {
            nextSchedule.add(Calendar.DAY_OF_YEAR, 1)
        }

        // 4. Return the difference
        return nextSchedule.timeInMillis - now.timeInMillis
    }

    // You can add other utility functions here, such as:
    // - Formatting Long time stamps into readable strings.
    // - Generating a list of all due doses for "Today's Schedule" screen.
}