package com.example.healthapp.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.healthapp.R

class AddRecordTypeDialog : DialogFragment() {

    interface OnRecordTypeSelectedListener {
        fun onRecordTypeSelected(type: String)
    }

    private var listener: OnRecordTypeSelectedListener? = null

    fun setOnRecordTypeSelectedListener(listener: OnRecordTypeSelectedListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val types = arrayOf("Давление", "Пульс", "Температура", "Вес", "Прививка", "Кислород")

        return AlertDialog.Builder(requireContext())
            .setTitle("Выберите тип записи")
            .setItems(types) { _, which ->
                val selectedType = when (which) {
                    0 -> "PRESSURE"
                    1 -> "PULSE"
                    2 -> "TEMPERATURE"  // ← ДОБАВИЛ
                    3 -> "WEIGHT"       // ← ДОБАВИЛ
                    4 -> "VACCINATION"
                    5 -> "OXYGEN"
                    else -> "PRESSURE"
                }
                listener?.onRecordTypeSelected(selectedType)
            }
            .setNegativeButton("Отмена", null)
            .create()
    }
}