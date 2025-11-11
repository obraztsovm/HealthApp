package com.example.healthapp.models

import java.util.Date

data class BodyMetrics(
    val weight: Double,
    val height: Double? = null,
    val bmi: Double? = null,
    val waist: Double? = null,
    val fatPercentage: Double? = null,
    override val id: Long = System.currentTimeMillis(),
    override val date: Date = Date(),
    override val notes: String = ""
) : HealthMetric(id, HealthCategory.BODY_METRICS, "WEIGHT", weight, "kg", "", date, notes) {
    fun calculateBMI(): Double {
        return if (height != null && height > 0) {
            weight / (height * height)
        } else 0.0
    }
}