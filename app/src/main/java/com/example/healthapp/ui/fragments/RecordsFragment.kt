package com.example.healthapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.example.healthapp.R
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.healthapp.databinding.FragmentRecordsBinding
import com.example.healthapp.repository.HealthRepository
import com.example.healthapp.ui.adapters.HealthRecordAdapter
import com.example.healthapp.ui.dialogs.AddRecordTypeDialog
import com.example.healthapp.ui.helpers.SwipeToDeleteCallback
import android.app.AlertDialog
import android.widget.Toast

class RecordsFragment : Fragment(), AddRecordTypeDialog.OnRecordTypeSelectedListener {

    private var _binding: FragmentRecordsBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: HealthRepository
    private lateinit var adapter: HealthRecordAdapter
    private var currentFilter: String = "ALL"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecordsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = HealthRepository(requireContext())
        setupRecyclerView()
        setupClickListeners()
        setupFilterChips()

        // Получаем фильтр из аргументов
        arguments?.getString("filter")?.let { filter ->
            currentFilter = filter
            setFilter(filter)
        } ?: loadAllRecords()
    }

    private fun setupRecyclerView() {
        adapter = HealthRecordAdapter()
        binding.recordsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recordsRecyclerView.adapter = adapter

        setupSwipeToDelete()
    }

    private fun setupSwipeToDelete() {
        val swipeToDeleteCallback = SwipeToDeleteCallback(adapter) { position ->
            showDeleteConfirmationDialog(position)
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.recordsRecyclerView)
    }

    private fun showDeleteConfirmationDialog(position: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Удалить запись?")
            .setMessage("Вы уверены, что хотите удалить эту запись?")
            .setPositiveButton("Удалить") { _, _ ->
                deleteRecord(position)
            }
            .setNegativeButton("Отмена") { _, _ ->
                adapter.notifyItemChanged(position)
            }
            .setCancelable(false)
            .show()
    }

    private fun deleteRecord(position: Int) {
        val deletedRecord = adapter.removeRecord(position)
        repository.deleteRecord(deletedRecord.id)

        when (currentFilter) {
            "ALL" -> updateStats(adapter.itemCount)
            "PRESSURE" -> updateStats(repository.getPressureRecords().size)
            "PULSE" -> updateStats(repository.getPulseRecords().size)
            "TEMPERATURE" -> updateStats(repository.getTemperatureRecords().size)
            "WEIGHT" -> updateStats(repository.getWeightRecords().size)
            "VACCINATION" -> updateStats(repository.getVaccinationRecords().size)
        }

        Toast.makeText(requireContext(), "Запись удалена", Toast.LENGTH_SHORT).show()
    }

    private fun setupClickListeners() {
        binding.addRecordButton.setOnClickListener {
            showRecordTypeDialog()
        }
    }

    private fun setupFilterChips() {
        binding.filterChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            when {
                checkedIds.contains(R.id.chipAll) -> setFilter("ALL")
                checkedIds.contains(R.id.chipPressure) -> setFilter("PRESSURE")
                checkedIds.contains(R.id.chipPulse) -> setFilter("PULSE")
                checkedIds.contains(R.id.chipTemperature) -> setFilter("TEMPERATURE")
                checkedIds.contains(R.id.chipWeight) -> setFilter("WEIGHT")
                checkedIds.contains(R.id.chipVaccination) -> setFilter("VACCINATION")
            }
        }
    }

    private fun setFilter(filter: String) {
        currentFilter = filter
        updateFilterChips()

        when (filter) {
            "ALL" -> loadAllRecords()
            "PRESSURE" -> loadPressureRecords()
            "PULSE" -> loadPulseRecords()
            "TEMPERATURE" -> loadTemperatureRecords()
            "WEIGHT" -> loadWeightRecords()
            "VACCINATION" -> loadVaccinationRecords()
        }
    }

    private fun updateFilterChips() {
        // ChipGroup автоматически управляет состоянием
    }

    private fun loadAllRecords() {
        val records = repository.getAllRecordsBlocking()
        adapter.setData(records)
        updateStats(records.size)
    }

    private fun loadPressureRecords() {
        val records = repository.getPressureRecords()
        adapter.setPressureData(records)
        updateStats(records.size)
    }

    private fun loadPulseRecords() {
        val records = repository.getPulseRecords()
        adapter.setPulseData(records)
        updateStats(records.size)
    }

    private fun loadTemperatureRecords() {
        val records = repository.getTemperatureRecords()
        adapter.setTemperatureData(records)
        updateStats(records.size)
    }

    private fun loadWeightRecords() {
        val records = repository.getWeightRecords()
        adapter.setWeightData(records)
        updateStats(records.size)
    }

    private fun loadVaccinationRecords() {
        val records = repository.getVaccinationRecords()
        adapter.setVaccinationData(records)
        updateStats(records.size)
    }

    private fun updateStats(recordsCount: Int) {
        binding.statsText.text = "Показано записей: $recordsCount"
    }

    private fun showRecordTypeDialog() {
        val dialog = AddRecordTypeDialog()
        dialog.setOnRecordTypeSelectedListener(this)
        dialog.show(parentFragmentManager, "RecordTypeDialog")
    }

    override fun onRecordTypeSelected(type: String) {
        //(activity as? MainActivity)?.onRecordTypeSelected(type)
        Toast.makeText(requireContext(), "Выбрано: $type", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}