package com.example.healthapp.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.healthapp.R
import com.example.healthapp.models.Vaccination

class AddVaccinationDialog : DialogFragment() {

    interface OnVaccinationAddedListener {
        fun onVaccinationAdded(vaccination: Vaccination)
    }

    private var listener: OnVaccinationAddedListener? = null

    fun setOnVaccinationAddedListener(listener: OnVaccinationAddedListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_add_vaccination, null)
        val editVaccineName = view.findViewById<EditText>(R.id.editVaccineName)
        val editDose = view.findViewById<EditText>(R.id.editDose)
        val editDoctor = view.findViewById<EditText>(R.id.editDoctor)
        val editLocation = view.findViewById<EditText>(R.id.editLocation)

        return AlertDialog.Builder(requireContext())
            .setTitle("Добавить прививку")
            .setView(view)
            .setPositiveButton("Сохранить") { _, _ ->
                val vaccineName = editVaccineName.text.toString().takeIf { it.isNotBlank() } ?: "Не указано"
                val dose = editDose.text.toString().takeIf { it.isNotBlank() } ?: "1 доза"
                val doctor = editDoctor.text.toString()
                val location = editLocation.text.toString()

                val vaccination = Vaccination(
                    vaccineName = vaccineName,
                    dose = dose,
                    doctor = doctor,
                    location = location
                )
                listener?.onVaccinationAdded(vaccination)
            }
            .setNegativeButton("Отмена", null)
            .create()
    }
}