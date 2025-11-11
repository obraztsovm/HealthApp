package com.example.healthapp.models

import java.util.Date

data class DoctorVisit(
    val doctorName: String,
    val specialization: String,
    val diagnosis: String = "",
    val nextVisit: Date? = null,
    override val id: Long = System.currentTimeMillis(),
    override val date: Date = Date(),
    override val notes: String = ""
) : HealthMetric(id, HealthCategory.DOCTORS_VISITS, "VISIT", 0.0, "", "", date, notes)