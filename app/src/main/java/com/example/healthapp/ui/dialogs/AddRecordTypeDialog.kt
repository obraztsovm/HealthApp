package com.example.healthapp.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.healthapp.R
import com.example.healthapp.models.HealthCategory

class AddRecordTypeDialog : DialogFragment() {

    interface OnRecordTypeSelectedListener {
        fun onRecordTypeSelected(category: HealthCategory)
    }

    private var listener: OnRecordTypeSelectedListener? = null

    fun setOnRecordTypeSelectedListener(listener: OnRecordTypeSelectedListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val types = arrayOf("Анализы крови", "Витамины", "Гормоны", "Прививки", "Показатели тела", "Врачи")

        return AlertDialog.Builder(requireContext())
            .setTitle("Выберите категорию")
            .setItems(types) { _, which ->
                val selectedCategory = when (which) {
                    0 -> HealthCategory.BLOOD_TESTS
                    1 -> HealthCategory.VITAMINS
                    2 -> HealthCategory.HORMONES
                    3 -> HealthCategory.VACCINATIONS
                    4 -> HealthCategory.BODY_METRICS
                    5 -> HealthCategory.DOCTORS_VISITS
                    else -> HealthCategory.BODY_METRICS
                }
                listener?.onRecordTypeSelected(selectedCategory)
            }
            .setNegativeButton("Отмена", null)
            .create()
    }
}