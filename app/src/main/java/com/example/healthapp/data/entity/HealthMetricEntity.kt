package com.example.healthapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "health_metrics")
data class HealthMetricEntity(
    @PrimaryKey val id: Long,
    val type: String,  // "PRESSURE", "PULSE" etc
    val value: Double,
    val date: Long,    // timestamp вместо Date
    val notes: String,

    // Для давления
    val systolic: Int? = null,
    val diastolic: Int? = null,
    val pulseValue: Int? = null,

    // Для пульса
    val beatsPerMinute: Int? = null
)