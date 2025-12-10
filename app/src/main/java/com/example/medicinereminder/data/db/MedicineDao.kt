package com.example.medicinereminder.data.db

import androidx.room.*
import com.example.medicinereminder.data.db.entities.DoseSchedule
import com.example.medicinereminder.data.db.entities.Medicine
import com.example.medicinereminder.data.db.entities.TakenDoseHistory
import kotlinx.coroutines.flow.Flow

// Data class to combine Medicine and History for the Home Screen
data class TodayDose(
    val medicineId: Long,
    val name: String,
    val dosage: String,
    val scheduleTime: String, // e.g., "08:00"
    val dueTime: Long,        // The specific time this dose is due today (epoch)
    val isTaken: Boolean?     // Null if pending, True if taken, False if missed
)

@Dao
interface MedicineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicine(medicine: Medicine): Long // Returns the new medicine ID

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoseSchedules(schedules: List<DoseSchedule>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoseHistory(history: TakenDoseHistory)

    // A simplified query for all history entries (for History Screen)
    @Query("""
        SELECT * FROM TakenDoseHistory AS h
        INNER JOIN Medicine AS m ON h.medicineId = m.id
        ORDER BY h.time DESC
    """)
    fun getAllHistory(): Flow<List<TakenDoseHistory>>

    // Needed by the Repository/Worker to get schedules
    @Query("SELECT * FROM DoseSchedule WHERE medicineId = :id")
    suspend fun getSchedulesForMedicine(id: Long): List<DoseSchedule>

    // IMPORTANT: In a real app, the query for today's doses is complex and often calculated
    // in the Repository, not just a simple Room query. The DAO provides the raw data.
    @Query("SELECT * FROM Medicine")
    fun getAllMedicine(): Flow<List<Medicine>>
}