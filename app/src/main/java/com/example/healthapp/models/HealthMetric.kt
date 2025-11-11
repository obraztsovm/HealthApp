package com.example.healthapp.models

import java.util.Date

open class HealthMetric(
    open val id: Long = System.currentTimeMillis(),
    open val category: HealthCategory,
    open val subType: String = "",
    open val value: Double,
    open val unit: String = "",
    open val referenceRange: String = "",
    open val date: Date = Date(),
    open val notes: String = ""
) {
    fun getFormattedDate(): String {
        return android.text.format.DateFormat.getDateFormat(android.app.Application().applicationContext).format(date)
    }

    fun getFormattedTime(): String {
        return android.text.format.DateFormat.getTimeFormat(android.app.Application().applicationContext).format(date)
    }
}