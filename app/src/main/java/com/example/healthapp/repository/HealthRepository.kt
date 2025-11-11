package com.example.healthapp.repository

import android.content.Context
import com.example.healthapp.data.database.HealthDatabase
import com.example.healthapp.data.entity.HealthMetricEntity
import com.example.healthapp.models.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class HealthRepository(private val context: Context) {
    private val database = HealthDatabase.getInstance(context)
    private val dao = database.healthMetricDao()


    suspend fun addRecord(record: HealthMetric) {
        val entity = when (record) {
            is BloodPressure -> HealthMetricEntity(
                id = record.id, type = MetricType.PRESSURE.name, value = record.value,
                date = record.date.time, notes = record.notes,
                systolic = record.systolic, diastolic = record.diastolic, pulseValue = record.pulse
            )
            is Pulse -> HealthMetricEntity(
                id = record.id, type = MetricType.PULSE.name, value = record.value,
                date = record.date.time, notes = record.notes,
                beatsPerMinute = record.beatsPerMinute
            )
            is Vaccination -> HealthMetricEntity(
                id = record.id, type = MetricType.VACCINATION.name, value = 0.0,
                date = record.date.time, notes = "Вакцина: ${record.vaccineName}, Доза: ${record.dose}, Врач: ${record.doctor}, Место: ${record.location}"
            )
            is Temperature -> HealthMetricEntity(  // ← ДОБАВЬ
                id = record.id, type = MetricType.TEMPERATURE.name, value = record.celsius,
                date = record.date.time, notes = record.notes
            )
            is Weight -> HealthMetricEntity(  // ← ДОБАВЬ
                id = record.id, type = MetricType.WEIGHT.name, value = record.kilograms,
                date = record.date.time, notes = record.notes
            )
            else -> HealthMetricEntity(
                id = record.id, type = record.type.name, value = record.value,
                date = record.date.time, notes = record.notes
            )
        }
        dao.insert(entity)
    }



    fun addRecordBlocking(record: HealthMetric) {
        runBlocking {
            addRecord(record)
        }
    }


    fun getPressureRecords(): List<BloodPressure> {
        return runBlocking {
            val entities = dao.getAll().first()
            entities
                .filter { it.type == MetricType.PRESSURE.name }
                .map { it.toHealthMetric() as BloodPressure }
        }
    }

    fun getPulseRecords(): List<Pulse> {
        return runBlocking {
            val entities = dao.getAll().first()
            entities
                .filter { it.type == MetricType.PULSE.name }
                .map { it.toHealthMetric() as Pulse }
        }
    }

    fun getVaccinationRecords(): List<Vaccination> {
        return runBlocking {
            val entities = dao.getAll().first()
            entities
                .filter { it.type == MetricType.VACCINATION.name }
                .map { it.toHealthMetric() as Vaccination }
        }
    }

    // Обнови метод для общего количества
    fun getRecordsCount(): Int {
        return runBlocking {
            dao.getAll().first().size
        }
    }

    // Добавь методы для получения записей по типам
    fun getTemperatureRecords(): List<Temperature> {
        return runBlocking {
            val entities = dao.getAll().first()
            entities
                .filter { it.type == MetricType.TEMPERATURE.name }
                .map { it.toHealthMetric() as Temperature }
        }
    }

    fun getWeightRecords(): List<Weight> {
        return runBlocking {
            val entities = dao.getAll().first()
            entities
                .filter { it.type == MetricType.WEIGHT.name }
                .map { it.toHealthMetric() as Weight }
        }
    }



    fun getAllRecordsBlocking(): List<HealthMetric> {
        return runBlocking {
            val entities = dao.getAll().first()
            entities.map { it.toHealthMetric() }
        }
    }

    fun getAllRecords(): Flow<List<HealthMetric>> {
        return dao.getAll().map { entities ->
            entities.map { it.toHealthMetric() }
        }
    }

    fun deleteRecord(recordId: Long) {
        runBlocking {
            dao.deleteById(recordId)
        }
    }
}

// Extension function
private fun HealthMetricEntity.toHealthMetric(): HealthMetric {
    return when (type) {
        MetricType.PRESSURE.name -> BloodPressure(
            systolic = systolic ?: 0, diastolic = diastolic ?: 0, pulse = pulseValue ?: 0,
            id = id, date = java.util.Date(date), notes = notes
        )
        MetricType.PULSE.name -> Pulse(
            beatsPerMinute = beatsPerMinute ?: 0,
            id = id, date = java.util.Date(date), notes = notes
        )
        MetricType.VACCINATION.name -> Vaccination(
            vaccineName = extractVaccineName(notes), dose = extractDose(notes),
            doctor = extractDoctor(notes), location = extractLocation(notes),
            id = id, date = java.util.Date(date), notes = notes
        )
        MetricType.TEMPERATURE.name -> Temperature(  // ← ДОБАВЬ
            celsius = value, id = id, date = java.util.Date(date), notes = notes
        )
        MetricType.WEIGHT.name -> Weight(  // ← ДОБАВЬ
            kilograms = value, id = id, date = java.util.Date(date), notes = notes
        )
        else -> HealthMetric(
            id = id, type = MetricType.valueOf(type), value = value,
            date = java.util.Date(date), notes = notes
        )
    }
}

// Добавь вспомогательные функции для парсинга прививок:
private fun extractVaccineName(notes: String): String {
    return notes.substringAfter("Вакцина: ").substringBefore(",")
}

private fun extractDose(notes: String): String {
    return notes.substringAfter("Доза: ").substringBefore(",")
}

private fun extractDoctor(notes: String): String {
    return notes.substringAfter("Врач: ").substringBefore(",")
}

private fun extractLocation(notes: String): String {
    return notes.substringAfter("Место: ")
}

