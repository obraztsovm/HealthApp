package com.example.healthapp.models

import java.util.Date

data class VitaminTest(
    val vitaminD: Double? = null,
    val vitaminB12: Double? = null,
    val iron: Double? = null,
    val magnesium: Double? = null,
    val calcium: Double? = null,
    override val id: Long = System.currentTimeMillis(),
    override val date: Date = Date(),
    override val notes: String = ""
) : HealthMetric(id, HealthCategory.VITAMINS, "VITAMIN_PANEL", 0.0, "", "", date, notes)