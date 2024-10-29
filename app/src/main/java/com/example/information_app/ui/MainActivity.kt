package com.example.information_app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.information_app.R

class MainActivity : AppCompatActivity() {
    private lateinit var nav_controller: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // set up action bar
        val nav_host_fragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        nav_controller = nav_host_fragment.findNavController()
        setupActionBarWithNavController(nav_controller)

    }

    // set up navigate back button on action bar
    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp() || nav_controller.navigateUp()
    }
}