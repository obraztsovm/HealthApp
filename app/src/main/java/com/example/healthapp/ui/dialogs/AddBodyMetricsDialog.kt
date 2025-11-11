package com.example.healthapp.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.DialogFragment
import android.widget.EditText
import com.example.healthapp.R
import com.example.healthapp.models.BodyMetrics

class AddBodyMetricsDialog : DialogFragment() {

    interface OnBodyMetricsAddedListener {
        fun onBodyMetricsAdded(bodyMetrics: BodyMetrics)
    }

    private var listener: OnBodyMetricsAddedListener? = null

    fun setOnBodyMetricsAddedListener(listener: OnBodyMetricsAddedListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_add_body_metrics, null)
        val editWeight = view.findViewById<EditText>(R.id.editWeight)
        val editHeight = view.findViewById<EditText>(R.id.editHeight)
        val editWaist = view.findViewById<EditText>(R.id.editWaist)
        val editFatPercentage = view.findViewById<EditText>(R.id.editFatPercentage)

        return AlertDialog.Builder(requireContext())
            .setTitle("Показатели тела")
            .setView(view)
            .setPositiveButton("Сохранить") { _, _ ->
                val weight = editWeight.text.toString().toDoubleOrNull() ?: 0.0
                val height = editHeight.text.toString().toDoubleOrNull()
                val waist = editWaist.text.toString().toDoubleOrNull()
                val fatPercentage = editFatPercentage.text.toString().toDoubleOrNull()

                val bodyMetrics = BodyMetrics(
                    weight = weight,
                    height = height,
                    waist = waist,
                    fatPercentage = fatPercentage
                )
                listener?.onBodyMetricsAdded(bodyMetrics)
            }
            .setNegativeButton("Отмена", null)
            .create()
    }
}