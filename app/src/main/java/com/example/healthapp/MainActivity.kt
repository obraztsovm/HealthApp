package com.example.healthapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.healthapp.ui.dialogs.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthapp.databinding.ActivityMainBinding
import com.example.healthapp.models.*
import com.example.healthapp.repository.HealthRepository
import com.example.healthapp.ui.adapters.HealthRecordAdapter
import com.example.healthapp.ui.dialogs.*
import android.app.AlertDialog
import android.widget.Toast
import com.example.healthapp.ui.fragments.PulseFragment
import com.example.healthapp.ui.fragments.TemperatureFragment
import com.example.healthapp.ui.fragments.VaccinationFragment
import com.example.healthapp.ui.fragments.WeightFragment
import com.example.healthapp.ui.fragments.PressureFragment
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.healthapp.ui.helpers.SwipeToDeleteCallback
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import com.example.healthapp.ui.fragments.DashboardFragment
import com.example.healthapp.ui.fragments.RecordsFragment

class MainActivity : AppCompatActivity(),
    AddRecordTypeDialog.OnRecordTypeSelectedListener,
    AddBloodPressureDialog.OnPressureAddedListener,
    AddPulseDialog.OnPulseAddedListener,
    AddVaccinationDialog.OnVaccinationAddedListener,
    AddTemperatureDialog.OnTemperatureAddedListener,
    AddWeightDialog.OnWeightAddedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var repository: HealthRepository
    private lateinit var adapter: HealthRecordAdapter
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    private var currentFilter: String = "ALL"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigationDrawer()
        repository = HealthRepository(this)

        // Загружаем главный экран по умолчанию
        showFragment(DashboardFragment())
    }

    private fun setupNavigationDrawer() {
        drawerLayout = binding.drawerLayout
        navView = binding.navView

        // Настройка кнопки меню в toolbar
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Обработка кликов в боковом меню
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_dashboard -> {
                    showFragment(DashboardFragment())
                    binding.toolbar.title = "HealthTracker"
                }
                R.id.nav_measurements -> {
                    showRecordsFragment()
                    binding.toolbar.title = "Все измерения"
                }
                R.id.nav_pressure -> {
                    showFragment(PressureFragment())
                    binding.toolbar.title = "Давление"
                }
                R.id.nav_pulse -> {
                    showFragment(PulseFragment())
                    binding.toolbar.title = "Пульс"
                }
                R.id.nav_temperature -> {
                    showFragment(TemperatureFragment())
                    binding.toolbar.title = "Температура"
                }
                R.id.nav_vaccination -> {
                    showFragment(VaccinationFragment())
                    binding.toolbar.title = "Прививки"
                }
                R.id.nav_weight -> {
                    showFragment(WeightFragment())
                    binding.toolbar.title = "Вес"
                }
                R.id.nav_statistics -> {
                    Toast.makeText(this, "Статистика в разработке", Toast.LENGTH_SHORT).show()
                }
            }
            drawerLayout.closeDrawers()
            true

        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_frame, fragment)
            .commit()
    }

    private fun showRecordsFragment() {
        val fragment = RecordsFragment().apply {
            arguments = Bundle().apply {
                putString("filter", "ALL")
            }
        }
        showFragment(fragment)
    }

    private fun showRecordsFragmentWithFilter(filter: String) {
        val fragment = RecordsFragment().apply {
            arguments = Bundle().apply {
                putString("filter", filter)
            }
        }
        showFragment(fragment)
    }

    fun showAddPulseDialog() {
        val dialog = AddPulseDialog()
        dialog.setOnPulseAddedListener(this)
        dialog.show(supportFragmentManager, "AddPulseDialog")
    }

    fun showAddTemperatureDialog() {
        val dialog = AddTemperatureDialog()
        dialog.setOnTemperatureAddedListener(this)
        dialog.show(supportFragmentManager, "AddTemperatureDialog")
    }

    fun showAddWeightDialog() {
        val dialog = AddWeightDialog()
        dialog.setOnWeightAddedListener(this)
        dialog.show(supportFragmentManager, "AddWeightDialog")
    }

    fun showAddVaccinationDialog() {
        val dialog = AddVaccinationDialog()
        dialog.setOnVaccinationAddedListener(this)
        dialog.show(supportFragmentManager, "AddVaccinationDialog")
    }

    fun showAddPressureDialog() {
        val dialog = AddBloodPressureDialog()
        dialog.setOnPressureAddedListener(this)
        dialog.show(supportFragmentManager, "AddPressureDialog")
    }

    // Остальной код ваших диалогов и обработчиков...
    override fun onRecordTypeSelected(type: String) {
        when (type) {
            "PRESSURE" -> showAddPressureDialog()
            "PULSE" -> showAddPulseDialog()
            "TEMPERATURE" -> showAddTemperatureDialog()
            "WEIGHT" -> showAddWeightDialog()
            "VACCINATION" -> showAddVaccinationDialog()
        }
    }


    override fun onPulseAdded(pulse: Pulse) {
        repository.addRecordBlocking(pulse)
    }

    override fun onVaccinationAdded(vaccination: Vaccination) {
        repository.addRecordBlocking(vaccination)
    }

    override fun onTemperatureAdded(temperature: Temperature) {
        repository.addRecordBlocking(temperature)
    }

    override fun onWeightAdded(weight: Weight) {
        repository.addRecordBlocking(weight)
    }

    override fun onPressureAdded(pressure: BloodPressure) {
        repository.addRecordBlocking(pressure)
        Toast.makeText(this, "Давление добавлено", Toast.LENGTH_SHORT).show()
    }
}