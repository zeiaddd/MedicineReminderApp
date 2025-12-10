package com.example.medicinereminder.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Medicine")
data class Medicine(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val dosage: String,
    val startDate: Long, // Use Long for epoch time
    val endDate: Long
)