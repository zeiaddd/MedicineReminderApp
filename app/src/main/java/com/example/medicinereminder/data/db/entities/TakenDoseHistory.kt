package com.example.medicinereminder.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "TakenDoseHistory",
    foreignKeys = [ForeignKey(
        entity = Medicine::class,
        parentColumns = ["id"],
        childColumns = ["medicineId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class TakenDoseHistory(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val medicineId: Long, // Foreign Key to Medicine
    val time: Long,       // Actual time the dose was due (epoch time)
    val taken: Boolean    // true for "Taken", false for "Missed"
)