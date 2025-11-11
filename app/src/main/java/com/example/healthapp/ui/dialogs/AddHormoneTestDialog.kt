package com.example.healthapp.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.healthapp.R
import com.example.healthapp.models.HormoneTest

class AddHormoneTestDialog : DialogFragment() {

    interface OnHormoneTestAddedListener {
        fun onHormoneTestAdded(hormoneTest: HormoneTest)
    }

    private var listener: OnHormoneTestAddedListener? = null

    fun setOnHormoneTestAddedListener(listener: OnHormoneTestAddedListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_add_hormone_test, null)
        val editTSH = view.findViewById<EditText>(R.id.editTSH)
        val editCortisol = view.findViewById<EditText>(R.id.editCortisol)
        val editTestosterone = view.findViewById<EditText>(R.id.editTestosterone)
        val editEstrogen = view.findViewById<EditText>(R.id.editEstrogen)

        return AlertDialog.Builder(requireContext())
            .setTitle("Гормоны")
            .setView(view)
            .setPositiveButton("Сохранить") { _, _ ->
                val hormoneTest = HormoneTest(
                    tsh = editTSH.text.toString().toDoubleOrNull(),
                    cortisol = editCortisol.text.toString().toDoubleOrNull(),
                    testosterone = editTestosterone.text.toString().toDoubleOrNull(),
                    estrogen = editEstrogen.text.toString().toDoubleOrNull()
                )
                listener?.onHormoneTestAdded(hormoneTest)
            }
            .setNegativeButton("Отмена", null)
            .create()
    }
}