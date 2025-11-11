package com.example.healthapp.data.dao

import androidx.room.*
import com.example.healthapp.data.entity.HealthMetricEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthMetricDao {

    @Query("SELECT * FROM health_metrics ORDER BY date DESC")
    fun getAll(): Flow<List<HealthMetricEntity>>

    @Query("SELECT * FROM health_metrics WHERE category = :category ORDER BY date DESC")
    fun getByCategory(category: String): Flow<List<HealthMetricEntity>>

    @Insert
    suspend fun insert(metric: HealthMetricEntity)

    @Delete
    suspend fun delete(metric: HealthMetricEntity)

    @Query("DELETE FROM health_metrics WHERE id = :id")
    suspend fun deleteById(id: Long)
}