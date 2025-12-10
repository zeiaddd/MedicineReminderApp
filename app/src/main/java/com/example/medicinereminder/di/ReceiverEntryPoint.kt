package com.example.medicinereminder.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.example.medicinereminder.data.repository.MedicineRepository

/**
 * Hilt Entry Point definition for the NotificationReceiver.
 * This tells Hilt exactly what dependency the Receiver needs.
 */
@EntryPoint
@InstallIn(SingletonComponent::class) // Install in the application scope
interface ReceiverEntryPoint {
    fun repository(): MedicineRepository
}