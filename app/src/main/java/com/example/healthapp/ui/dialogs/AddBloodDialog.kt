package com.example.healthapp.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.DialogFragment
import com.example.healthapp.R
import com.example.healthapp.models.BloodTest

class AddBloodTestDialog : DialogFragment() {

    interface OnBloodTestAddedListener {
        fun onBloodTestAdded(bloodTest: BloodTest)
    }

    private var listener: OnBloodTestAddedListener? = null

    fun setOnBloodTestAddedListener(listener: OnBloodTestAddedListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_add_blood_test, null)
        val editHemoglobin = view.findViewById<EditText>(R.id.editHemoglobin)
        val editLeukocytes = view.findViewById<EditText>(R.id.editLeukocytes)
        val editPlatelets = view.findViewById<EditText>(R.id.editPlatelets)
        val editGlucose = view.findViewById<EditText>(R.id.editGlucose)
        val editCholesterol = view.findViewById<EditText>(R.id.editCholesterol)

        return AlertDialog.Builder(requireContext())
            .setTitle("Анализы крови")
            .setView(view)
            .setPositiveButton("Сохранить") { _, _ ->
                val bloodTest = BloodTest(
                    hemoglobin = editHemoglobin.text.toString().toDoubleOrNull(),
                    leukocytes = editLeukocytes.text.toString().toDoubleOrNull(),
                    platelets = editPlatelets.text.toString().toDoubleOrNull(),
                    glucose = editGlucose.text.toString().toDoubleOrNull(),
                    cholesterol = editCholesterol.text.toString().toDoubleOrNull()
                )
                listener?.onBloodTestAdded(bloodTest)
            }
            .setNegativeButton("Отмена", null)
            .create()
    }
}