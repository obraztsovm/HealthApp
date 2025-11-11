// ui/viewmodels/HealthViewModelFactory.kt
package com.example.healthapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.healthapp.repository.HealthRepository

class HealthViewModelFactory(
    private val repository: HealthRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HealthViewModel(repository) as T
    }
}