package com.example.healthapp.models

import java.util.Date

data class BloodPressure(
    val systolic: Int,
    val diastolic: Int,
    val pulse: Int,
    override val id: Long = System.currentTimeMillis(),
    override val date: Date = Date(),
    override val notes: String = ""
) : HealthMetric(id, MetricType.PRESSURE, systolic.toDouble(), date, notes) {
    fun getFormattedPressure(): String = "$systolic/$diastolic"

    fun getPressureStatus(): String {
        return when {
            systolic < 90 || diastolic < 60 -> "Пониженное"
            systolic in 90..119 && diastolic in 60..79 -> "Нормальное"
            systolic in 120..129 && diastolic < 80 -> "Повышенное"
            systolic in 130..139 || diastolic in 80..89 -> "1 степень"
            systolic >= 140 || diastolic >= 90 -> "2 степень"
            else -> "Не определено"
        }
    }
}