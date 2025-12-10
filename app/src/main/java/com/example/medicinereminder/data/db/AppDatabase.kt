package com.example.medicinereminder.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.medicinereminder.data.db.entities.* // Adjust your package name

@Database(
    entities = [Medicine::class, DoseSchedule::class, TakenDoseHistory::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // Abstract function Room uses to implement the DAO
    abstract fun medicineDao(): MedicineDao

    // 🔑 FIX: Ensure this static creation method exists inside the companion object
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Hilt calls this method via the AppModule to provide a singleton instance of the database.
         */
        fun create(context: Context): AppDatabase { // ⬅️ THIS is the 'create' function
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "medicine_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}