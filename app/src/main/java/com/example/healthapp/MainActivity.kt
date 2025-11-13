package com.example.healthapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.healthapp.databinding.ActivityMainBinding
import com.example.healthapp.models.*
import com.example.healthapp.repository.HealthRepository
import com.example.healthapp.ui.dialogs.*
import com.example.healthapp.ui.fragments.*
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.ActionBarDrawerToggle

class MainActivity : AppCompatActivity(),
    AddRecordTypeDialog.OnRecordTypeSelectedListener,
    AddBloodTestDialog.OnBloodTestAddedListener,
    AddVitaminTestDialog.OnVitaminTestAddedListener,
    AddBodyMetricsDialog.OnBodyMetricsAddedListener,
    AddDoctorVisitDialog.OnDoctorVisitAddedListener,
    AddHormoneTestDialog.OnHormoneTestAddedListener,
    AddVaccinationDialog.OnVaccinationAddedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var repository: HealthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = HealthRepository(this)
        setupNavigationDrawer()
        showFragment(DashboardFragment())
    }

    private fun setupNavigationDrawer() {
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_dashboard -> {
                    showFragment(DashboardFragment())
                    binding.toolbar.title = "HealthTracker"
                }
                R.id.nav_blood_tests -> {
                    showRecordsFragment("BLOOD_TESTS")
                    binding.toolbar.title = "Анализы крови"
                }
                R.id.nav_vitamins -> {
                    showRecordsFragment("VITAMINS")
                    binding.toolbar.title = "Витамины"
                }
                R.id.nav_hormones -> {
                    showRecordsFragment("HORMONES")
                    binding.toolbar.title = "Гормоны"
                }
                R.id.nav_body_metrics -> {
                    showRecordsFragment("BODY_METRICS")
                    binding.toolbar.title = "Показатели тела"
                }
                R.id.nav_doctors -> {
                    showRecordsFragment("DOCTORS_VISITS")
                    binding.toolbar.title = "Врачи"
                }
                R.id.nav_vaccinations -> {
                    showRecordsFragment("VACCINATIONS")
                    binding.toolbar.title = "Прививки"
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

    private fun showRecordsFragment(category: String) {
        val fragment = RecordsFragment().apply {
            arguments = Bundle().apply {
                putString("category", category)
            }
        }
        showFragment(fragment)
    }


    fun showAddHormoneTestDialog() {
        val dialog = AddHormoneTestDialog()
        dialog.setOnHormoneTestAddedListener(this)
        dialog.show(supportFragmentManager, "AddHormoneTestDialog")
    }


    fun showAddVaccinationDialog() {
        val dialog = AddVaccinationDialog()
        dialog.setOnVaccinationAddedListener(this)
        dialog.show(supportFragmentManager, "AddVaccinationDialog")
    }


    fun showAddBloodTestDialog() {
        val dialog = AddBloodTestDialog()
        dialog.setOnBloodTestAddedListener(this)
        dialog.show(supportFragmentManager, "AddBloodTestDialog")
    }

    fun showAddVitaminTestDialog() {
        val dialog = AddVitaminTestDialog()
        dialog.setOnVitaminTestAddedListener(this)
        dialog.show(supportFragmentManager, "AddVitaminTestDialog")
    }

    fun showAddBodyMetricsDialog() {
        val dialog = AddBodyMetricsDialog()
        dialog.setOnBodyMetricsAddedListener(this)
        dialog.show(supportFragmentManager, "AddBodyMetricsDialog")
    }

    fun showAddDoctorVisitDialog() {
        val dialog = AddDoctorVisitDialog()
        dialog.setOnDoctorVisitAddedListener(this)
        dialog.show(supportFragmentManager, "AddDoctorVisitDialog")
    }

    // Обработчики выбора типа записи
    override fun onRecordTypeSelected(category: HealthCategory) {
        when (category) {
            HealthCategory.BLOOD_TESTS -> showAddBloodTestDialog()
            HealthCategory.VITAMINS -> showAddVitaminTestDialog()
            HealthCategory.HORMONES -> showAddHormoneTestDialog()
            HealthCategory.BODY_METRICS -> showAddBodyMetricsDialog()
            HealthCategory.DOCTORS_VISITS -> showAddDoctorVisitDialog()
            HealthCategory.VACCINATIONS -> showAddVaccinationDialog()
            }

    }

    override fun onVaccinationAdded(vaccination: Vaccination) {
        repository.addRecordBlocking(vaccination)
        Toast.makeText(this, "Прививка добавлена", Toast.LENGTH_SHORT).show()
    }



    // Обработчики добавления записей
    override fun onBloodTestAdded(bloodTest: BloodTest) {
        repository.addRecordBlocking(bloodTest)
        Toast.makeText(this, "Анализы крови добавлены", Toast.LENGTH_SHORT).show()
    }

    override fun onHormoneTestAdded(hormoneTest: HormoneTest) {
        repository.addRecordBlocking(hormoneTest)
        Toast.makeText(this, "Гормоны добавлены", Toast.LENGTH_SHORT).show()
    }



    override fun onVitaminTestAdded(vitaminTest: VitaminTest) {
        repository.addRecordBlocking(vitaminTest)
        Toast.makeText(this, "Витамины добавлены", Toast.LENGTH_SHORT).show()
    }

    override fun onBodyMetricsAdded(bodyMetrics: BodyMetrics) {
        repository.addRecordBlocking(bodyMetrics)
        Toast.makeText(this, "Показатели тела добавлены", Toast.LENGTH_SHORT).show()
    }

    override fun onDoctorVisitAdded(doctorVisit: DoctorVisit) {
        repository.addRecordBlocking(doctorVisit)
        Toast.makeText(this, "Визит к врачу добавлен", Toast.LENGTH_SHORT).show()
    }
}