package com.example.healthapp.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.healthapp.MainActivity
import com.example.healthapp.R
import com.example.healthapp.databinding.FragmentDashboardBinding
import com.example.healthapp.models.HealthCategory
import com.example.healthapp.repository.HealthRepository
import com.example.healthapp.ui.dialogs.AddRecordTypeDialog
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DashboardFragment : Fragment(), AddRecordTypeDialog.OnRecordTypeSelectedListener {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: HealthRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = HealthRepository(requireContext())
        setupCharts()
        setupClickListeners()
        setupDataObserver()
    }

    private fun setupClickListeners() {
        binding.addRecordButton.setOnClickListener {
            showRecordTypeDialog()
        }
    }

    private fun showRecordTypeDialog() {
        val dialog = AddRecordTypeDialog()
        dialog.setOnRecordTypeSelectedListener(this)
        dialog.show(parentFragmentManager, "RecordTypeDialog")
    }

    // Наблюдатель за изменениями данных
    private fun setupDataObserver() {
        lifecycleScope.launch {
            repository.getAllRecords().collect { records ->
                // Автоматически обновляем графики при изменении данных
                refreshAllCharts()
            }
        }
    }

    private fun setupCharts() {
        setupWeightChart()
        setupBloodChart()
        setupPieChart()
    }

    // Обновляем все графики
    private fun refreshAllCharts() {
        setupWeightChart()
        setupBloodChart()
        setupPieChart()
    }

    private fun setupWeightChart() {
        val weightRecords = repository.getBodyMetrics().takeLast(7)

        if (weightRecords.size >= 2) {
            val entries = ArrayList<Entry>()
            val dates = ArrayList<String>()

            weightRecords.forEachIndexed { index, bodyMetrics ->
                entries.add(Entry(index.toFloat(), bodyMetrics.weight.toFloat()))
                dates.add(SimpleDateFormat("dd.MM", Locale.getDefault()).format(bodyMetrics.date))
            }

            val dataSet = LineDataSet(entries, "Вес (кг)")
            dataSet.color = Color.parseColor("#1565C0")
            dataSet.valueTextColor = Color.parseColor("#2C3E50")
            dataSet.lineWidth = 3f
            dataSet.setCircleColor(Color.parseColor("#1565C0"))
            dataSet.circleRadius = 4f
            dataSet.setDrawFilled(true)
            dataSet.fillColor = Color.parseColor("#E3F2FD")
            dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER

            val lineData = LineData(dataSet)
            binding.weightChart.data = lineData

            // Настройка внешнего вида
            binding.weightChart.description.isEnabled = false
            binding.weightChart.legend.isEnabled = false
            binding.weightChart.setTouchEnabled(true)
            binding.weightChart.setDrawGridBackground(false)

            val xAxis = binding.weightChart.xAxis
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.valueFormatter = IndexAxisValueFormatter(dates)
            xAxis.setDrawGridLines(false)
            xAxis.granularity = 1f

            binding.weightChart.axisLeft.setDrawGridLines(false)
            binding.weightChart.axisRight.isEnabled = false

            binding.weightChart.animateXY(1000, 1000)
        } else {
            // Заглушка если мало данных
            binding.weightChart.clear()
            binding.weightChart.data = null
        }
        binding.weightChart.invalidate()
    }

    private fun setupBloodChart() {
        val bloodRecords = repository.getBloodTests().takeLast(5)

        if (bloodRecords.isNotEmpty()) {
            val latestRecord = bloodRecords.last()
            val entries = ArrayList<BarEntry>()
            val labels = ArrayList<String>()

            var index = 0f
            latestRecord.hemoglobin?.let {
                entries.add(BarEntry(index++, it.toFloat()))
                labels.add("Hb")
            }

            latestRecord.glucose?.let {
                entries.add(BarEntry(index++, it.toFloat()))
                labels.add("Глюкоза")
            }

            latestRecord.cholesterol?.let {
                entries.add(BarEntry(index++, it.toFloat()))
                labels.add("Холест.")
            }

            if (entries.isNotEmpty()) {
                val dataSet = BarDataSet(entries, "Анализы")
                dataSet.colors = listOf(
                    Color.parseColor("#FF6B6B"),
                    Color.parseColor("#4ECDC4"),
                    Color.parseColor("#45B7D1")
                )

                val barData = BarData(dataSet)
                binding.bloodChart.data = barData

                val xAxis = binding.bloodChart.xAxis
                xAxis.valueFormatter = IndexAxisValueFormatter(labels)

                // Настройка внешнего вида
                binding.bloodChart.description.isEnabled = false
                binding.bloodChart.legend.isEnabled = false
                binding.bloodChart.setTouchEnabled(true)
                binding.bloodChart.setDrawGridBackground(false)

                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                xAxis.granularity = 1f

                binding.bloodChart.axisLeft.setDrawGridLines(false)
                binding.bloodChart.axisRight.isEnabled = false

                binding.bloodChart.animateY(1000)
            }
        } else {
            binding.bloodChart.clear()
            binding.bloodChart.data = null
        }
        binding.bloodChart.invalidate()
    }

    private fun setupPieChart() {
        val records = repository.getAllRecordsBlocking()

        // Считаем распределение по категориям
        val categoryCount = mutableMapOf<HealthCategory, Int>()
        HealthCategory.values().forEach { category ->
            categoryCount[category] = 0
        }

        records.forEach { record ->
            categoryCount[record.category] = categoryCount.getOrDefault(record.category, 0) + 1
        }

        val entries = ArrayList<PieEntry>()
        val colors = ArrayList<Int>()

        categoryCount.forEach { (category, count) ->
            if (count > 0) {
                entries.add(PieEntry(count.toFloat(), getCategoryLabel(category)))
                colors.add(getCategoryColor(category))
            }
        }

        if (entries.isNotEmpty()) {
            val dataSet = PieDataSet(entries, "")
            dataSet.colors = colors
            dataSet.valueTextSize = 12f
            dataSet.valueTextColor = Color.WHITE

            val pieData = PieData(dataSet)
            pieData.setValueFormatter(object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return value.toInt().toString()
                }
            })

            binding.pieChart.data = pieData
            binding.pieChart.description.isEnabled = false
            binding.pieChart.legend.isEnabled = false
            binding.pieChart.setDrawEntryLabels(false)
            binding.pieChart.setUsePercentValues(false)
            binding.pieChart.setDrawHoleEnabled(true)
            binding.pieChart.holeRadius = 40f
            binding.pieChart.transparentCircleRadius = 45f
            binding.pieChart.setHoleColor(Color.TRANSPARENT)

            binding.pieChart.animateY(1000)
        } else {
            binding.pieChart.clear()
            binding.pieChart.data = null
        }
        binding.pieChart.invalidate()
    }

    private fun getCategoryLabel(category: HealthCategory): String {
        return when (category) {
            HealthCategory.BLOOD_TESTS -> "Анализы"
            HealthCategory.VITAMINS -> "Витамины"
            HealthCategory.HORMONES -> "Гормоны"
            HealthCategory.VACCINATIONS -> "Прививки"
            HealthCategory.BODY_METRICS -> "Тело"
            HealthCategory.DOCTORS_VISITS -> "Врачи"
        }
    }

    private fun getCategoryColor(category: HealthCategory): Int {
        return when (category) {
            HealthCategory.BLOOD_TESTS -> Color.parseColor("#FF6B6B")
            HealthCategory.VITAMINS -> Color.parseColor("#4ECDC4")
            HealthCategory.HORMONES -> Color.parseColor("#45B7D1")
            HealthCategory.VACCINATIONS -> Color.parseColor("#96CEB4")
            HealthCategory.BODY_METRICS -> Color.parseColor("#FFEAA7")
            HealthCategory.DOCTORS_VISITS -> Color.parseColor("#DDA0DD")
        }
    }





    override fun onRecordTypeSelected(category: HealthCategory) {
        (activity as? MainActivity)?.onRecordTypeSelected(category)

        // После добавления записи обновляем графики
        lifecycleScope.launch {
            refreshAllCharts()
        }
    }

    override fun onResume() {
        super.onResume()
        // Обновляем графики при возвращении на экран
        refreshAllCharts()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}