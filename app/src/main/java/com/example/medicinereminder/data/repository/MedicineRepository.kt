package com.example.medicinereminder.data.repository

import com.example.medicinereminder.data.db.MedicineDao
import com.example.medicinereminder.data.db.entities.*
import com.example.medicinereminder.domain.DoseAlarmWorker
import com.example.medicinereminder.util.ScheduleCalculator
import androidx.work.WorkManager // Import the class
import androidx.work.*
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit
import javax.inject.Inject // 🔑 Crucial HILT IMPORT

// 💡 FIX: Add @Inject to the constructor
class MedicineRepository @Inject constructor(
    private val dao: MedicineDao,
    private val workManager: WorkManager
) {
    // 1. Save new medicine and schedules
    suspend fun saveMedicineAndSchedule(
        medicine: Medicine,
        times: List<String> // e.g., ["08:00", "14:00"]
    ) {
        // Save Medicine and get the new ID
        val medicineId = dao.insertMedicine(medicine)

        // Create DoseSchedule entries
        val schedules = times.map { timeString ->
            DoseSchedule(
                medicineId = medicineId,
                time = timeString,
                frequency = times.size
            )
        }
        dao.insertDoseSchedules(schedules)

        // Schedule all notifications for this medicine
        scheduleDoseAlarms(medicineId, schedules)
    }

    // 2. Insert into history when "Taken" or "Missed" is clicked
    suspend fun updateDoseHistory(medicineId: Long, timeDue: Long, taken: Boolean) {
        dao.insertDoseHistory(TakenDoseHistory(
            medicineId = medicineId,
            time = timeDue,
            taken = taken
        ))
    }

    // 3. Get data for UI (from DAO)
    fun getAllMedicines(): Flow<List<Medicine>> = dao.getAllMedicine()
    fun getAllHistory(): Flow<List<TakenDoseHistory>> = dao.getAllHistory()

    // 4. Scheduling Logic (Uses injected WorkManager)
    private fun scheduleDoseAlarms(medicineId: Long, schedules: List<DoseSchedule>) {
        // Use the injected workManager instance directly

        // Cancel any previous alarms for this medicine if updated
        workManager.cancelAllWorkByTag(medicineId.toString())

        for (schedule in schedules) {
            // Calculate the initial delay from now until the next scheduled time
            val initialDelay = ScheduleCalculator.calculateInitialDelay(schedule.time)

            // Define the input data for the Worker
            val inputData = Data.Builder()
                .putLong(DoseAlarmWorker.KEY_MEDICINE_ID, medicineId)
                .putString(DoseAlarmWorker.KEY_SCHEDULE_TIME, schedule.time)
                .build()

            val doseWorkRequest = PeriodicWorkRequestBuilder<DoseAlarmWorker>(
                repeatInterval = 24, // Repeat every 24 hours
                repeatIntervalTimeUnit = TimeUnit.HOURS
            )
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .setInputData(inputData)
                .addTag(medicineId.toString())
                .build()

            workManager.enqueue(doseWorkRequest)
        }
    }
}