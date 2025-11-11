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

    fun setData(newRecords: List<HealthMetric>) {
        records.clear()
        records.addAll(newRecords)
        notifyDataSetChanged()
    }


    // Метод для добавления только давления
    fun setPressureData(pressureRecords: List<BloodPressure>) {
        records.clear()
        records.addAll(pressureRecords)
        notifyDataSetChanged()
    }
    fun removeRecord(position: Int): HealthMetric {
        val removedRecord = records[position]
        records.removeAt(position)
        notifyItemRemoved(position)
        return removedRecord
    }



    // Метод для добавления только пульса
    fun setPulseData(pulseRecords: List<Pulse>) {
        records.clear()
        records.addAll(pulseRecords)
        notifyDataSetChanged()
    }

    // Метод для добавления только прививок
    fun setVaccinationData(vaccinationRecords: List<Vaccination>) {
        records.clear()
        records.addAll(vaccinationRecords)
        notifyDataSetChanged()
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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textType: TextView = itemView.findViewById(R.id.textType)
        private val textValue: TextView = itemView.findViewById(R.id.textValue)
        private val textDate: TextView = itemView.findViewById(R.id.textDate)

        fun bind(record: HealthMetric) {
            textType.text = when (record.type) {
                MetricType.PRESSURE -> "📊 Давление"
                MetricType.PULSE -> "💓 Пульс"
                MetricType.TEMPERATURE -> "🌡️ Температура"  // ← ДОБАВЬ
                MetricType.WEIGHT -> "⚖️ Вес"              // ← ДОБАВЬ
                MetricType.VACCINATION -> "💉 Прививка"
                else -> record.type.name
            }

            textValue.text = when (record) {
                is BloodPressure -> "${record.getFormattedPressure()}"
                is Pulse -> "${record.beatsPerMinute} уд/мин"
                is Temperature -> "${record.celsius}°C"      // ← ДОБАВЬ
                is Weight -> "${record.kilograms} кг"        // ← ДОБАВЬ
                is Vaccination -> "${record.vaccineName} - ${record.dose}"
                else -> record.value.toString()
            }

            textDate.text = SimpleDateFormat("dd.MM HH:mm", Locale.getDefault())
                .format(record.date)
        }
    }

    // Добавь методы в адаптер для новых типов
    fun setTemperatureData(temperatureRecords: List<Temperature>) {
        records.clear()
        records.addAll(temperatureRecords)
        notifyDataSetChanged()
    }

    fun setWeightData(weightRecords: List<Weight>) {
        records.clear()
        records.addAll(weightRecords)
        notifyDataSetChanged()
    }
}