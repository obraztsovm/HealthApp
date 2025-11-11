package com.example.healthapp.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.DialogFragment
import com.example.healthapp.R
import android.widget.EditText
import com.example.healthapp.models.DoctorVisit

class AddDoctorVisitDialog : DialogFragment() {

    interface OnDoctorVisitAddedListener {
        fun onDoctorVisitAdded(doctorVisit: DoctorVisit)
    }

    private var listener: OnDoctorVisitAddedListener? = null

    fun setOnDoctorVisitAddedListener(listener: OnDoctorVisitAddedListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_add_doctor_visit, null)
        val editDoctorName = view.findViewById<EditText>(R.id.editDoctorName)
        val editSpecialization = view.findViewById<EditText>(R.id.editSpecialization)
        val editDiagnosis = view.findViewById<EditText>(R.id.editDiagnosis)

        return AlertDialog.Builder(requireContext())
            .setTitle("Визит к врачу")
            .setView(view)
            .setPositiveButton("Сохранить") { _, _ ->
                val doctorVisit = DoctorVisit(
                    doctorName = editDoctorName.text.toString(),
                    specialization = editSpecialization.text.toString(),
                    diagnosis = editDiagnosis.text.toString()
                )
                listener?.onDoctorVisitAdded(doctorVisit)
            }
            .setNegativeButton("Отмена", null)
            .create()
    }
}