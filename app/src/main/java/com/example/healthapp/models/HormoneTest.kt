package com.example.healthapp.models

import java.util.Date

data class HormoneTest(
    val tsh: Double? = null,
    val cortisol: Double? = null,
    val testosterone: Double? = null,
    val estrogen: Double? = null,
    override val id: Long = System.currentTimeMillis(),
    override val date: Date = Date(),
    override val notes: String = ""
) : HealthMetric(id, HealthCategory.HORMONES, "HORMONE_PANEL", 0.0, "", "", date, notes)