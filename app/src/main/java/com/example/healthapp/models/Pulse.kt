package com.example.healthapp.models

import java.util.Date

data class Pulse(
    val beatsPerMinute: Int,
    override val id: Long = System.currentTimeMillis(),
    override val date: Date = Date(),
    override val notes: String = ""
) : HealthMetric(id, MetricType.PULSE, beatsPerMinute.toDouble(), date, notes) {
    fun getPulseStatus(): String {
        return when (beatsPerMinute) {
            in 0..59 -> "Замедленный"
            in 60..100 -> "Нормальный"
            else -> "Учащенный"
        }
    }
}