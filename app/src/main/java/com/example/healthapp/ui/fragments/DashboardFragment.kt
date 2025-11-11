package com.example.healthapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.healthapp.R

class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val addButton = view.findViewById<android.widget.Button>(R.id.addRecordButton)
        addButton.setOnClickListener {
            // Пока просто покажем сообщение
            android.widget.Toast.makeText(requireContext(), "Добавить запись", android.widget.Toast.LENGTH_SHORT).show()
        }

        return view
    }
}