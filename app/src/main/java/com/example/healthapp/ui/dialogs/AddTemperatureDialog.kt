package com.example.healthapp.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.healthapp.R
import com.example.healthapp.models.Temperature

class AddTemperatureDialog : DialogFragment() {

    interface OnTemperatureAddedListener {
        fun onTemperatureAdded(temperature: Temperature)
    }

    private var listener: OnTemperatureAddedListener? = null

    fun setOnTemperatureAddedListener(listener: OnTemperatureAddedListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_add_temperature, null)
        val editTemperature = view.findViewById<EditText>(R.id.editTemperature)

        return AlertDialog.Builder(requireContext())
            .setTitle("Добавить температуру")
            .setView(view)
            .setPositiveButton("Сохранить") { _, _ ->
                val temp = editTemperature.text.toString().toDoubleOrNull() ?: 36.6

                val temperature = Temperature(celsius = temp)
                listener?.onTemperatureAdded(temperature)
            }
            .setNegativeButton("Отмена", null)
            .create()
    }
}