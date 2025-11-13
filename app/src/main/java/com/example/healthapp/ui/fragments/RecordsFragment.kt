package com.example.healthapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthapp.databinding.FragmentRecordsBinding
import com.example.healthapp.repository.HealthRepository
import com.example.healthapp.ui.adapters.HealthRecordAdapter

class RecordsFragment : Fragment() {

    private var _binding: FragmentRecordsBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: HealthRepository
    private lateinit var adapter: HealthRecordAdapter
    private var currentCategory: String = ""

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

        // Получаем категорию из аргументов
        currentCategory = arguments?.getString("category") ?: ""
        loadRecords()
    }

    private fun setupRecyclerView() {
        adapter = HealthRecordAdapter()
        binding.recordsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recordsRecyclerView.adapter = adapter

        // Обработка удаления
        adapter.onItemDelete = { position ->
            deleteRecord(position)
        }
    }

    private fun loadRecords() {
        val records = when (currentCategory) {
            "BLOOD_TESTS" -> repository.getBloodTests()
            "VITAMINS" -> repository.getVitaminTests()
            "HORMONES" -> repository.getHormoneTests()
            "BODY_METRICS" -> repository.getBodyMetrics()
            "DOCTORS_VISITS" -> repository.getDoctorVisits()
            "VACCINATIONS" -> repository.getVaccinationRecords()
            else -> repository.getAllRecordsBlocking()
        }
        adapter.setData(records)
        updateStats(records.size)
    }

    private fun deleteRecord(position: Int) {
        val deletedRecord = adapter.removeRecord(position)
        repository.deleteRecord(deletedRecord.id)
        updateStats(adapter.itemCount)
    }

    private fun updateStats(recordsCount: Int) {
        binding.statsText.text = "Записей: $recordsCount"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}