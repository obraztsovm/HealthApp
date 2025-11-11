package com.example.healthapp.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.healthapp.R
import com.example.healthapp.models.Weight

class AddWeightDialog : DialogFragment() {

    interface OnWeightAddedListener {
        fun onWeightAdded(weight: Weight)
    }

    private var listener: OnWeightAddedListener? = null

    fun setOnWeightAddedListener(listener: OnWeightAddedListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_add_weight, null)
        val editWeight = view.findViewById<EditText>(R.id.editWeight)

        return AlertDialog.Builder(requireContext())
            .setTitle("Добавить вес")
            .setView(view)
            .setPositiveButton("Сохранить") { _, _ ->
                val weightValue = editWeight.text.toString().toDoubleOrNull() ?: 70.0

                val weight = Weight(kilograms = weightValue)
                listener?.onWeightAdded(weight)
            }
            .setNegativeButton("Отмена", null)
            .create()
    }
}