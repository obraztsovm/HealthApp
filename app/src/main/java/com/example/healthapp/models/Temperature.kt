package com.example.healthapp.models

import java.util.*

data class Temperature(
    val celsius: Double,
    override val id: Long = System.currentTimeMillis(),
    override val date: Date = Date(),
    override val notes: String = ""
) : HealthMetric(id, MetricType.TEMPERATURE, celsius, date, notes) {

    fun getTemperatureStatus(): String {
        return when {
            celsius < 36.0 -> "Пониженная"
            celsius in 36.0..37.0 -> "Нормальная"
            celsius in 37.1..38.0 -> "Повышенная"
            else -> "Высокая"
        }
    }
}