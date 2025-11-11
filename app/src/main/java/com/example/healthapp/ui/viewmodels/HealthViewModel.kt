package com.example.healthapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthapp.models.HealthMetric
import com.example.healthapp.repository.HealthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HealthViewModel(private val repository: HealthRepository) : ViewModel() {

    private val _records = MutableStateFlow<List<HealthMetric>>(emptyList())
    val records: StateFlow<List<HealthMetric>> = _records.asStateFlow()

    init {
        loadRecords()
    }

    private fun loadRecords() {
        viewModelScope.launch {
            repository.getAllRecords().collect { recordsList ->
                _records.value = recordsList
            }
        }
    }

    fun addRecord(record: HealthMetric) {
        viewModelScope.launch {
            repository.addRecord(record)
            // После добавления автоматически обновятся записи через Flow
        }
    }
}