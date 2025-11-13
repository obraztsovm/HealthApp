package com.example.healthapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthapp.R
import com.example.healthapp.models.*
import java.text.SimpleDateFormat
import java.util.*

class HealthRecordAdapter : RecyclerView.Adapter<HealthRecordAdapter.ViewHolder>() {

    private val records = mutableListOf<HealthMetric>()
    var onItemDelete: ((position: Int) -> Unit)? = null

    fun setData(newRecords: List<HealthMetric>) {
        records.clear()
        records.addAll(newRecords)
        notifyDataSetChanged()
    }

    fun removeRecord(position: Int): HealthMetric {
        val removedRecord = records[position]
        records.removeAt(position)
        notifyItemRemoved(position)
        return removedRecord
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_health_record, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(records[position])
    }

    override fun getItemCount() = records.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textType: TextView = itemView.findViewById(R.id.textType)
        private val textValue: TextView = itemView.findViewById(R.id.textValue)
        private val textDate: TextView = itemView.findViewById(R.id.textDate)

        fun bind(record: HealthMetric) {
            textType.text = when (record.category) {
                HealthCategory.BLOOD_TESTS -> "🩸 Анализы крови"
                HealthCategory.VITAMINS -> "💊 Витамины"
                HealthCategory.HORMONES -> "⚖️ Гормоны"
                HealthCategory.VACCINATIONS -> "💉 Прививка"
                HealthCategory.BODY_METRICS -> "📏 Показатели тела"
                HealthCategory.DOCTORS_VISITS -> "👨‍⚕️ Визит к врачу"
            }

            textValue.text = when (record) {
                is BloodTest -> formatBloodTest(record)
                is VitaminTest -> formatVitaminTest(record)
                is HormoneTest -> formatHormoneTest(record)
                is Vaccination -> "${record.vaccineName} - ${record.dose}"
                is BodyMetrics -> "${record.weight} кг" +
                        (record.bmi?.let { ", ИМТ: ${String.format("%.1f", it)}" } ?: "")
                is DoctorVisit -> "Др. ${record.doctorName} - ${record.specialization}"
                else -> record.value.toString()
            }

            textDate.text = SimpleDateFormat("dd.MM HH:mm", Locale.getDefault())
                .format(record.date)

            // Долгое нажатие для удаления
            itemView.setOnLongClickListener {
                onItemDelete?.invoke(adapterPosition)
                true
            }
        }

        private fun formatBloodTest(bloodTest: BloodTest): String {
            val values = listOfNotNull(
                bloodTest.hemoglobin?.let { "Hb: ${it}g/dL" },
                bloodTest.glucose?.let { "Глюкоза: ${it}ммоль/л" }
            )
            return values.take(2).joinToString(", ")
        }

        private fun formatVitaminTest(vitaminTest: VitaminTest): String {
            val values = listOfNotNull(
                vitaminTest.vitaminD?.let { "D: ${it}нг/мл" },
                vitaminTest.iron?.let { "Железо: ${it}мкг/дл" }
            )
            return values.take(2).joinToString(", ")
        }

        private fun formatHormoneTest(hormoneTest: HormoneTest): String {
            val values = listOfNotNull(
                hormoneTest.tsh?.let { "ТТГ: ${it}мкМЕ/мл" },
                hormoneTest.testosterone?.let { "Тестост.: ${it}нг/дл" }
            )
            return values.take(2).joinToString(", ")
        }

        
    }
}