package com.example.healthapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.healthapp.databinding.ActivityMainBinding
import com.example.healthapp.ui.fragments.*
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.ActionBarDrawerToggle

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                R.id.nav_measurements -> {
                    showFragment(SimpleRecordsFragment())
                    binding.toolbar.title = "Все измерения"
                }
                R.id.nav_blood_tests -> {
                    showFragment(SimpleRecordsFragment())
                    binding.toolbar.title = "Анализы крови"
                }
                R.id.nav_vitamins -> {
                    showFragment(SimpleRecordsFragment())
                    binding.toolbar.title = "Витамины"
                }
                R.id.nav_body_metrics -> {
                    showFragment(SimpleRecordsFragment())
                    binding.toolbar.title = "Показатели тела"
                }
                R.id.nav_doctors -> {
                    showFragment(SimpleRecordsFragment())
                    binding.toolbar.title = "Врачи"
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


}