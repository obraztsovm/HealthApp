package com.example.healthapp.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.healthapp.R
import com.example.healthapp.models.VitaminTest

class AddVitaminTestDialog : DialogFragment() {

    interface OnVitaminTestAddedListener {
        fun onVitaminTestAdded(vitaminTest: VitaminTest)
    }

    private var listener: OnVitaminTestAddedListener? = null

    fun setOnVitaminTestAddedListener(listener: OnVitaminTestAddedListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_add_vitamin_test, null)
        val editVitaminD = view.findViewById<EditText>(R.id.editVitaminD)
        val editVitaminB12 = view.findViewById<EditText>(R.id.editVitaminB12)
        val editIron = view.findViewById<EditText>(R.id.editIron)
        val editMagnesium = view.findViewById<EditText>(R.id.editMagnesium)
        val editCalcium = view.findViewById<EditText>(R.id.editCalcium)

        return AlertDialog.Builder(requireContext())
            .setTitle("Витамины и минералы")
            .setView(view)
            .setPositiveButton("Сохранить") { _, _ ->
                val vitaminTest = VitaminTest(
                    vitaminD = editVitaminD.text.toString().toDoubleOrNull(),
                    vitaminB12 = editVitaminB12.text.toString().toDoubleOrNull(),
                    iron = editIron.text.toString().toDoubleOrNull(),
                    magnesium = editMagnesium.text.toString().toDoubleOrNull(),
                    calcium = editCalcium.text.toString().toDoubleOrNull()
                )
                listener?.onVitaminTestAdded(vitaminTest)
            }
            .setNegativeButton("Отмена", null)
            .create()
    }
}