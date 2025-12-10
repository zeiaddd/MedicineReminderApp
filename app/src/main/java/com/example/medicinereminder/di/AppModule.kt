package com.example.medicinereminder.di

import android.content.Context
import androidx.work.WorkManager
import com.example.medicinereminder.data.db.AppDatabase
import com.example.medicinereminder.data.db.MedicineDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 🔑 Hilt Module: Contains instructions for providing dependencies
 * that Hilt cannot construct itself (like Room DB and DAO).
 */
@Module
@InstallIn(SingletonComponent::class) // This scope lives as long as the application
object AppModule {

    // 1. Provides the Room Database instance
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        // We use the corrected static method in AppDatabase
        return AppDatabase.create(context)
    }

    // 2. Provides the Medicine DAO (This fixes the last MissingBinding error!)
    @Provides
    fun provideMedicineDao(database: AppDatabase): MedicineDao {
        return database.medicineDao()
    }

    // 3. Provides the WorkManager instance
    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }
}


