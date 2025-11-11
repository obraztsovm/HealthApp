//package com.example.healthapp.ui.fragments
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import com.example.healthapp.R
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.ItemTouchHelper
//import com.example.healthapp.databinding.FragmentRecordsBinding
//import com.example.healthapp.repository.HealthRepository
//import com.example.healthapp.ui.adapters.HealthRecordAdapter
//import com.example.healthapp.ui.dialogs.AddRecordTypeDialog
//import com.example.healthapp.ui.helpers.SwipeToDeleteCallback
//import android.app.AlertDialog
//import android.widget.Toast
//
//class RecordsFragment : Fragment(), AddRecordTypeDialog.OnRecordTypeSelectedListener {
//
//    private var _binding: FragmentRecordsBinding? = null
//    private val binding get() = _binding!!
//    private lateinit var repository: HealthRepository
//    private lateinit var adapter: HealthRecordAdapter
//    private var currentFilter: String = "ALL"
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentRecordsBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        repository = HealthRepository(requireContext())
//        setupRecyclerView()
//        setupClickListeners()
//        setupFilterChips()
//
//        // Получаем фильтр из аргументов
//        arguments?.getString("filter")?.let { filter ->
//            currentFilter = filter
//            setFilter(filter)
//        } ?: loadAllRecords()
//    }
//
//    private fun setupRecyclerView() {
//        adapter = HealthRecordAdapter()
//        binding.recordsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//        binding.recordsRecyclerView.adapter = adapter
//
//        setupSwipeToDelete()
//    }
//
//    private fun setupSwipeToDelete() {
//        val swipeToDeleteCallback = SwipeToDeleteCallback(adapter) { position ->
//            showDeleteConfirmationDialog(position)
//        }
//        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
//        itemTouchHelper.attachToRecyclerView(binding.recordsRecyclerView)
//    }
//
//    private fun showDeleteConfirmationDialog(position: Int) {
//        AlertDialog.Builder(requireContext())
//            .setTitle("Удалить запись?")
//            .setMessage("Вы уверены, что хотите удалить эту запись?")
//            .setPositiveButton("Удалить") { _, _ ->
//                deleteRecord(position)
//            }
//            .setNegativeButton("Отмена") { _, _ ->
//                adapter.notifyItemChanged(position)
//            }
//            .setCancelable(false)
//            .show()
//    }
//
//    private fun deleteRecord(position: Int) {
//        val deletedRecord = adapter.removeRecord(position)
//        repository.deleteRecord(deletedRecord.id)
//
//
//        private fun setupFilterChips() {
//            binding.filterChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
//                when {
//                    checkedIds.contains(R.id.chipAll) -> setFilter("ALL")
//                    checkedIds.contains(R.id.chipBloodTests) -> setFilter("BLOOD_TESTS")
//                    checkedIds.contains(R.id.chipVitamins) -> setFilter("VITAMINS")
//                    checkedIds.contains(R.id.chipHormones) -> setFilter("HORMONES")
//                    checkedIds.contains(R.id.chipBodyMetrics) -> setFilter("BODY_METRICS")
//                    checkedIds.contains(R.id.chipDoctors) -> setFilter("DOCTORS_VISITS")
//                    checkedIds.contains(R.id.chipVaccinations) -> setFilter("VACCINATIONS")
//                }
//            }
//        }
//
//        private fun setFilter(filter: String) {
//            currentFilter = filter
//            when (filter) {
//                "ALL" -> loadAllRecords()
//                "BLOOD_TESTS" -> loadBloodTests()
//                "VITAMINS" -> loadVitaminTests()
//                "HORMONES" -> loadHormoneTests()
//                "BODY_METRICS" -> loadBodyMetrics()
//                "DOCTORS_VISITS" -> loadDoctorVisits()
//                "VACCINATIONS" -> loadVaccinationRecords()
//            }
//        }
//
//        private fun loadBloodTests() {
//            val records = repository.getBloodTests()
//            adapter.setBloodTestData(records)
//            updateStats(records.size)
//        }
//
//        private fun loadVitaminTests() {
//            val records = repository.getVitaminTests()
//            adapter.setVitaminTestData(records)
//            updateStats(records.size)
//        }
//
//        private fun loadHormoneTests() {
//            val records = repository.getHormoneTests()
//            adapter.setHormoneTestData(records)
//            updateStats(records.size)
//        }
//
//        private fun loadBodyMetrics() {
//            val records = repository.getBodyMetrics()
//            adapter.setBodyMetricsData(records)
//            updateStats(records.size)
//        }
//
//        private fun loadDoctorVisits() {
//            val records = repository.getDoctorVisits()
//            adapter.setDoctorVisitData(records)
//            updateStats(records.size)
//        }
//
//        private fun loadAllRecords() {
//            val records = repository.getAllRecordsBlocking()
//            adapter.setData(records)
//            updateStats(records.size)
//        }
//
//    }
//}