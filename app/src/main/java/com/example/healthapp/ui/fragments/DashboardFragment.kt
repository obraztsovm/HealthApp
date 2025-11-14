package com.example.healthapp.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.healthapp.MainActivity
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarData
import com.example.healthapp.R
import com.example.healthapp.databinding.FragmentDashboardBinding
import com.example.healthapp.models.HealthCategory
import com.example.healthapp.repository.HealthRepository
import com.example.healthapp.ui.dialogs.AddRecordTypeDialog
import com.github.mikephil.charting.charts.LineChart
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
        setupStatisticsCards()
        setupWeightChart()
        setupBloodChart()
        setupPieChart()
        setupSparklineCharts()
        setupRadarChart()
        setupProgressChart()
        setupHeatMap()
    }

    // Обновляем все графики
    private fun refreshAllCharts() {
        setupStatisticsCards()
        setupWeightChart()
        setupBloodChart()
        setupPieChart()
        setupSparklineCharts()
        setupRadarChart()
        setupProgressChart()
        setupHeatMap()
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


    private fun setupSparklineCharts() {
        setupSparklineWeight()
        setupSparklineGlucose()
    }

    private fun setupSparklineWeight() {
        val weights = repository.getBodyMetrics().takeLast(7).map { it.weight.toFloat() }
        if (weights.size >= 2) {
            val entries = weights.mapIndexed { index, weight -> Entry(index.toFloat(), weight) }
            val dataSet = LineDataSet(entries, "").apply {
                color = Color.parseColor("#1565C0")
                lineWidth = 2f
                setDrawCircles(false)
                setDrawValues(false)
                mode = LineDataSet.Mode.CUBIC_BEZIER
            }
            binding.sparklineWeight.data = LineData(dataSet)
            setupSparklineAppearance(binding.sparklineWeight)
        }
    }

    private fun setupSparklineGlucose() {
        val glucoseValues = repository.getBloodTests().takeLast(7).mapNotNull { it.glucose?.toFloat() }
        if (glucoseValues.size >= 2) {
            val entries = glucoseValues.mapIndexed { index, value -> Entry(index.toFloat(), value) }
            val dataSet = LineDataSet(entries, "").apply {
                color = Color.parseColor("#FF6B6B")
                lineWidth = 2f
                setDrawCircles(false)
                setDrawValues(false)
                mode = LineDataSet.Mode.CUBIC_BEZIER
            }
            binding.sparklineGlucose.data = LineData(dataSet)
            setupSparklineAppearance(binding.sparklineGlucose)
        }
    }

    private fun setupSparklineAppearance(chart: LineChart) {
        chart.description.isEnabled = false
        chart.legend.isEnabled = false
        chart.setTouchEnabled(false)
        chart.setDrawGridBackground(false)
        chart.xAxis.isEnabled = false
        chart.axisLeft.isEnabled = false
        chart.axisRight.isEnabled = false
        chart.invalidate()
    }

    private fun setupRadarChart() {
        val latestBlood = repository.getBloodTests().lastOrNull()
        val latestVitamins = repository.getVitaminTests().lastOrNull()

        // Используем RadarEntry вместо Entry
        val entries = ArrayList<RadarEntry>()

        latestBlood?.hemoglobin?.toFloat()?.let { entries.add(RadarEntry(it)) }
        latestBlood?.glucose?.toFloat()?.let { entries.add(RadarEntry(it)) }
        latestVitamins?.vitaminD?.toFloat()?.let { entries.add(RadarEntry(it)) }
        latestVitamins?.iron?.toFloat()?.let { entries.add(RadarEntry(it)) }

        if (entries.isNotEmpty()) {
            val dataSet = RadarDataSet(entries, "Показатели").apply {
                color = Color.parseColor("#FF6B6B")
                fillColor = Color.parseColor("#30FF6B6B")
                setDrawFilled(true)
                lineWidth = 2f
                setDrawValues(false)
            }

            binding.radarChart.data = RadarData(dataSet)
            binding.radarChart.webLineWidth = 1f
            binding.radarChart.description.isEnabled = false
            binding.radarChart.legend.isEnabled = false

            // Настройка осей
            val xAxis = binding.radarChart.xAxis
            xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return when (value.toInt()) {
                        0 -> "Гемоглобин"
                        1 -> "Глюкоза"
                        2 -> "Витамин D"
                        3 -> "Железо"
                        else -> ""
                    }
                }
            }

            binding.radarChart.animateXY(1000, 1000)
        } else {
            binding.radarChart.clear()
            binding.radarChart.data = null
        }
        binding.radarChart.invalidate()
    }

    private fun setupProgressChart() {
        val totalRecords = repository.getAllRecordsBlocking().size
        val weeklyRecords = repository.getAllRecordsBlocking()
            .count { record ->
                val weekAgo = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -7) }.time
                record.date.after(weekAgo)
            }

        val goal = 10 // Целевое количество записей в неделю
        val progress = if (weeklyRecords < goal) weeklyRecords.toFloat() else goal.toFloat() // Заменяем min()

        val entries = listOf(
            PieEntry(progress, "Выполнено"),
            PieEntry((goal - progress).toFloat(), "Осталось")
        )

        val dataSet = PieDataSet(entries, "").apply {
            colors = listOf(Color.parseColor("#4ECDC4"), Color.parseColor("#E0E0E0"))
            setDrawValues(false)
        }

        binding.progressChart.data = PieData(dataSet)
        binding.progressChart.description.isEnabled = false
        binding.progressChart.legend.isEnabled = false
        binding.progressChart.setDrawEntryLabels(false)
        binding.progressChart.setUsePercentValues(false)
        binding.progressChart.setDrawHoleEnabled(true)
        binding.progressChart.holeRadius = 60f
        binding.progressChart.transparentCircleRadius = 0f
        binding.progressChart.setHoleColor(Color.TRANSPARENT)

        binding.progressText.text = "$weeklyRecords из $goal записей за неделю"
        binding.progressChart.invalidate()
    }

    private fun setupStatisticsCards() {
        val totalRecords = repository.getAllRecordsBlocking().size
        val weeklyRecords = repository.getAllRecordsBlocking()
            .count { record ->
                val weekAgo = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -7) }.time
                record.date.after(weekAgo)
            }

        binding.totalRecordsText.text = totalRecords.toString()
        binding.lastWeekText.text = weeklyRecords.toString()
    }

    private fun setupHeatMap() {
        val records = repository.getAllRecordsBlocking()
        val dayCounts = MutableList(7) { 0 }

        records.forEach { record ->
            val calendar = Calendar.getInstance().apply { time = record.date }
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1 // 0-6 (воскресенье=0)
            dayCounts[dayOfWeek] = dayCounts[dayOfWeek] + 1
        }

        val entries = dayCounts.mapIndexed { index, count -> BarEntry(index.toFloat(), count.toFloat()) }

        if (entries.any { it.y > 0 }) {
            val dataSet = BarDataSet(entries, "Активность").apply {
                colors = dayCounts.map { count ->
                    when {
                        count >= 5 -> Color.parseColor("#1565C0")
                        count >= 3 -> Color.parseColor("#42A5F5")
                        count >= 1 -> Color.parseColor("#90CAF9")
                        else -> Color.parseColor("#E3F2FD")
                    }
                }
                setDrawValues(false)
            }

            binding.heatMapChart.data = BarData(dataSet)
            binding.heatMapChart.description.isEnabled = false
            binding.heatMapChart.legend.isEnabled = false
            binding.heatMapChart.setTouchEnabled(false)
            binding.heatMapChart.setDrawGridBackground(false)

            val xAxis = binding.heatMapChart.xAxis
            xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return when (value.toInt()) {
                        0 -> "Вс"
                        1 -> "Пн"
                        2 -> "Вт"
                        3 -> "Ср"
                        4 -> "Чт"
                        5 -> "Пт"
                        6 -> "Сб"
                        else -> ""
                    }
                }
            }
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            xAxis.granularity = 1f

            binding.heatMapChart.axisLeft.setDrawGridLines(false)
            binding.heatMapChart.axisRight.isEnabled = false

            binding.heatMapChart.animateY(1000)
        } else {
            binding.heatMapChart.clear()
            binding.heatMapChart.data = null
        }
        binding.heatMapChart.invalidate()
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