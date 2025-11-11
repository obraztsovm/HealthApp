package com.example.healthapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.healthapp.databinding.FragmentDashboardBinding
import com.example.healthapp.ui.dialogs.AddRecordTypeDialog

class DashboardFragment : Fragment(), AddRecordTypeDialog.OnRecordTypeSelectedListener {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.addRecordButton.setOnClickListener {
            showRecordTypeDialog()
        }
    }

    private fun showRecordTypeDialog() {
        val dialog = AddRecordTypeDialog()
        dialog.setOnRecordTypeSelectedListener(this)
        dialog.show(parentFragmentManager, "RecordTypeDialog")
    }

    override fun onRecordTypeSelected(type: String) {
        // Обработка будет в MainActivity
        //(activity as? MainActivity)?.onRecordTypeSelected(type)
        Toast.makeText(requireContext(), "Выбрано: $type", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}