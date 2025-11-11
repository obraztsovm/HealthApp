package com.example.healthapp.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.healthapp.R
import com.example.healthapp.models.BloodPressure

class AddBloodPressureDialog : DialogFragment() {

    interface OnPressureAddedListener {
        fun onPressureAdded(pressure: BloodPressure)
    }

    private var listener: OnPressureAddedListener? = null

    fun setOnPressureAddedListener(listener: OnPressureAddedListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_add_pressure, null)
        val editSystolic = view.findViewById<EditText>(R.id.editSystolic)
        val editDiastolic = view.findViewById<EditText>(R.id.editDiastolic)

        return AlertDialog.Builder(requireContext())
            .setTitle("Добавить давление")
            .setView(view)
            .setPositiveButton("Сохранить") { _, _ ->
                val systolic = editSystolic.text.toString().toIntOrNull() ?: 120
                val diastolic = editDiastolic.text.toString().toIntOrNull() ?: 80

                val pressure = BloodPressure(systolic, diastolic, 0) // Пульс = 0, т.к. не спрашиваем
                listener?.onPressureAdded(pressure)
            }
            .setNegativeButton("Отмена", null)
            .create()
    }
}