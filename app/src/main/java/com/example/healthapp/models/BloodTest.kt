package com.example.healthapp.models

import java.util.Date

data class BloodTest(
    val hemoglobin: Double? = null,
    val leukocytes: Double? = null,
    val platelets: Double? = null,
    val glucose: Double? = null,
    val cholesterol: Double? = null,
    override val id: Long = System.currentTimeMillis(),
    override val date: Date = Date(),
    override val notes: String = ""
) : HealthMetric(id, HealthCategory.BLOOD_TESTS, "BLOOD_PANEL", 0.0, "", "", date, notes)