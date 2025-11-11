package com.example.healthapp.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.healthapp.R
import com.example.healthapp.models.Pulse

class AddPulseDialog : DialogFragment() {

    interface OnPulseAddedListener {
        fun onPulseAdded(pulse: Pulse)
    }

    private var listener: OnPulseAddedListener? = null

    fun setOnPulseAddedListener(listener: OnPulseAddedListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_add_pulse, null)
        val editPulse = view.findViewById<EditText>(R.id.editPulse)

        return AlertDialog.Builder(requireContext())
            .setTitle("Добавить пульс")
            .setView(view)
            .setPositiveButton("Сохранить") { _, _ ->
                val pulseValue = editPulse.text.toString().toIntOrNull() ?: 72

                val pulse = Pulse(beatsPerMinute = pulseValue)
                listener?.onPulseAdded(pulse)
            }
            .setNegativeButton("Отмена", null)
            .create()
    }
}