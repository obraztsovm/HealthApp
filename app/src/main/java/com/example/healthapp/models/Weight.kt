package com.example.healthapp.models

import java.util.*

data class Weight(
    val kilograms: Double,
    override val id: Long = System.currentTimeMillis(),
    override val date: Date = Date(),
    override val notes: String = ""
) : HealthMetric(id, MetricType.WEIGHT, kilograms, date, notes)