package com.example.healthapp.repository

import android.content.Context
import com.example.healthapp.data.database.HealthDatabase
import com.example.healthapp.data.entity.HealthMetricEntity
import com.example.healthapp.models.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.util.Date

class HealthRepository(private val context: Context) {
    private val database = HealthDatabase.getInstance(context)
    private val dao = database.healthMetricDao()


    suspend fun addRecord(record: HealthMetric) {
        val entity = when (record) {
            is BloodTest -> HealthMetricEntity(
                id = record.id, category = HealthCategory.BLOOD_TESTS.name,
                value = 0.0, date = record.date.time, notes = record.notes,
                hemoglobin = record.hemoglobin, leukocytes = record.leukocytes,
                platelets = record.platelets, glucose = record.glucose,
                cholesterol = record.cholesterol
            )
            is VitaminTest -> HealthMetricEntity(
                id = record.id, category = HealthCategory.VITAMINS.name,
                value = 0.0, date = record.date.time, notes = record.notes,
                vitaminD = record.vitaminD, vitaminB12 = record.vitaminB12,
                iron = record.iron, magnesium = record.magnesium, calcium = record.calcium
            )
            is BodyMetrics -> HealthMetricEntity(
                id = record.id, category = HealthCategory.BODY_METRICS.name,
                value = record.weight, date = record.date.time, notes = record.notes,
                weight = record.weight, height = record.height, bmi = record.bmi,
                waist = record.waist, fatPercentage = record.fatPercentage
            )
            is HormoneTest -> HealthMetricEntity(
                id = record.id, category = HealthCategory.HORMONES.name,
                value = 0.0, date = record.date.time, notes = record.notes,
                tsh = record.tsh, cortisol = record.cortisol,
                testosterone = record.testosterone, estrogen = record.estrogen
            )
            is DoctorVisit -> HealthMetricEntity(
                id = record.id, category = HealthCategory.DOCTORS_VISITS.name,
                value = 0.0, date = record.date.time, notes = record.notes,
                doctorName = record.doctorName, specialization = record.specialization,
                diagnosis = record.diagnosis, nextVisit = record.nextVisit?.time
            )
            else -> HealthMetricEntity(
                id = record.id, category = record.category.name,
                value = record.value, date = record.date.time, notes = record.notes
            )
        }
        dao.insert(entity)
    }



    fun addRecordBlocking(record: HealthMetric) {
        runBlocking {
            addRecord(record)
        }
    }






    fun getBloodTests(): List<BloodTest> {
        return runBlocking {
            val entities = dao.getAll().first()
            entities
                .filter { it.category == HealthCategory.BLOOD_TESTS.name }
                .mapNotNull {
                    try {
                        it.toHealthMetric() as? BloodTest
                    } catch (e: Exception) {
                        null // Пропускаем некорректные записи
                    }
                }
        }
    }

    fun getHormoneTests(): List<HormoneTest> {
        return runBlocking {
            val entities = dao.getAll().first()
            entities
                .filter { it.category == HealthCategory.HORMONES.name }
                .mapNotNull {
                    try {
                        it.toHealthMetric() as? HormoneTest
                    } catch (e: Exception) {
                        null
                    }
                }
        }
    }

    fun getVitaminTests(): List<VitaminTest> {
        return runBlocking {
            val entities = dao.getAll().first()
            entities
                .filter { it.category == HealthCategory.VITAMINS.name }
                .mapNotNull {
                    try {
                        it.toHealthMetric() as? VitaminTest
                    } catch (e: Exception) {
                        null
                    }
                }
        }
    }

    fun getDoctorVisits(): List<DoctorVisit> {
        return runBlocking {
            val entities = dao.getAll().first()
            entities
                .filter { it.category == HealthCategory.DOCTORS_VISITS.name }
                .mapNotNull {
                    try {
                        it.toHealthMetric() as? DoctorVisit
                    } catch (e: Exception) {
                        null
                    }
                }
        }
    }

    fun getVaccinationRecords(): List<Vaccination> {
        return runBlocking {
            val entities = dao.getAll().first()
            entities
                .filter { it.category == HealthCategory.VACCINATIONS.name }
                .mapNotNull {
                    try {
                        it.toHealthMetric() as? Vaccination
                    } catch (e: Exception) {
                        null
                    }
                }
        }
    }

    fun getBodyMetrics(): List<BodyMetrics> {
        return runBlocking {
            val entities = dao.getAll().first()
            entities
                .filter { it.category == HealthCategory.BODY_METRICS.name }
                .mapNotNull {
                    try {
                        it.toHealthMetric() as? BodyMetrics
                    } catch (e: Exception) {
                        null
                    }
                }
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
    return when (category) {
        HealthCategory.BLOOD_TESTS.name -> BloodTest(
            hemoglobin = hemoglobin, leukocytes = leukocytes,
            platelets = platelets, glucose = glucose, cholesterol = cholesterol,
            id = id, date = Date(date), notes = notes
        )
        HealthCategory.VITAMINS.name -> VitaminTest(
            vitaminD = vitaminD, vitaminB12 = vitaminB12,
            iron = iron, magnesium = magnesium, calcium = calcium,
            id = id, date = Date(date), notes = notes
        )
        HealthCategory.BODY_METRICS.name -> BodyMetrics(
            weight = weight ?: 0.0, height = height,
            bmi = bmi, waist = waist, fatPercentage = fatPercentage,
            id = id, date = Date(date), notes = notes
        )
        HealthCategory.HORMONES.name -> HormoneTest(
            tsh = tsh, cortisol = cortisol,
            testosterone = testosterone, estrogen = estrogen,
            id = id, date = Date(date), notes = notes
        )
        HealthCategory.VACCINATIONS.name -> Vaccination(
            vaccineName = extractVaccineName(notes),
            dose = extractDose(notes),
            doctor = extractDoctor(notes),
            location = extractLocation(notes),
            id = id, date = Date(date), notes = notes
        )
        HealthCategory.DOCTORS_VISITS.name -> DoctorVisit(
            doctorName = doctorName, specialization = specialization,
            diagnosis = diagnosis, nextVisit = nextVisit?.let { Date(it) },
            id = id, date = Date(date), notes = notes
        )
        else -> HealthMetric(
            id = id, category = HealthCategory.valueOf(category),
            value = value, date = Date(date), notes = notes
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

