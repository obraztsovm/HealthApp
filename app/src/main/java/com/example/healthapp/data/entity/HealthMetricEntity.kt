package com.example.healthapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "health_metrics")
data class HealthMetricEntity(
    @PrimaryKey val id: Long,
    val category: String,  // Заменили type на category
    val value: Double,
    val date: Long,
    val notes: String,

    // Общие поля для всех анализов
    val subType: String = "", // "HEMOGLOBIN", "VITAMIN_D", "TESTOSTERONE" etc
    val unit: String = "",    // "g/dL", "ng/mL", "mmol/L" etc
    val referenceRange: String = "", // "12.0-16.0"

    // Для анализов крови
    val hemoglobin: Double? = null,
    val leukocytes: Double? = null,
    val platelets: Double? = null,
    val glucose: Double? = null,
    val cholesterol: Double? = null,

    // Для витаминов
    val vitaminD: Double? = null,
    val vitaminB12: Double? = null,
    val iron: Double? = null,
    val magnesium: Double? = null,
    val calcium: Double? = null,

    // Для гормонов
    val tsh: Double? = null,
    val cortisol: Double? = null,
    val testosterone: Double? = null,
    val estrogen: Double? = null,

    // Для тела
    val weight: Double? = null,
    val height: Double? = null,
    val bmi: Double? = null,
    val waist: Double? = null,
    val fatPercentage: Double? = null,

    // Для врачей
    val doctorName: String = "",
    val specialization: String = "",
    val diagnosis: String = "",
    val nextVisit: Long? = null
)