package com.example.healthapp.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.healthapp.data.dao.HealthMetricDao
import com.example.healthapp.data.entity.HealthMetricEntity

@Database(
    entities = [HealthMetricEntity::class],
    version = 3,
    exportSchema = false
)
abstract class HealthDatabase : RoomDatabase() {
    abstract fun healthMetricDao(): HealthMetricDao

    companion object {
        @Volatile
        private var INSTANCE: HealthDatabase? = null

        fun getInstance(context: Context): HealthDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HealthDatabase::class.java,
                    "health_database"
                )
                    .fallbackToDestructiveMigration()  // Временно для разработки
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}