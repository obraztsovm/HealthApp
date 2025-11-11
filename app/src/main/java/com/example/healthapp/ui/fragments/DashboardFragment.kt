package com.example.healthapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.healthapp.MainActivity
import com.example.healthapp.R
import com.example.healthapp.models.HealthCategory
import com.example.healthapp.ui.dialogs.AddRecordTypeDialog

class DashboardFragment : Fragment(), AddRecordTypeDialog.OnRecordTypeSelectedListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val addButton = view.findViewById<android.widget.Button>(R.id.addRecordButton)
        addButton.setOnClickListener {
            showRecordTypeDialog()
        }

        return view
    }

    private fun showRecordTypeDialog() {
        val dialog = AddRecordTypeDialog()
        dialog.setOnRecordTypeSelectedListener(this)
        dialog.show(parentFragmentManager, "RecordTypeDialog")
    }

    override fun onRecordTypeSelected(category: HealthCategory) {
        // Делегируем в MainActivity
        (activity as? MainActivity)?.onRecordTypeSelected(category)
    }
}