package com.example.healthapp.models

import java.util.*

data class Vaccination(
    val vaccineName: String,
    val dose: String,
    val doctor: String = "",
    val location: String = "",
    override val id: Long = System.currentTimeMillis(),
    override val date: Date = Date(),
    override val notes: String = ""
) : HealthMetric(id, MetricType.VACCINATION, 0.0, date, notes)