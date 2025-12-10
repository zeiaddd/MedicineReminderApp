package com.example.medicinereminder.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "DoseSchedule",
    // Defines the relationship with the Medicine table
    foreignKeys = [ForeignKey(
        entity = Medicine::class,
        parentColumns = ["id"],
        childColumns = ["medicineId"],
        onDelete = ForeignKey.CASCADE // If Medicine is deleted, delete its schedules
    )]
)
data class DoseSchedule(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val medicineId: Long, // Foreign Key to Medicine
    val time: String,      // e.g., "08:00", "14:00"
    val frequency: Int     // Total times per day (e.g., 2)
)