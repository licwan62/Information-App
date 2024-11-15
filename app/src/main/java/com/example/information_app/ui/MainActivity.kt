package com.example.information_app.ui

import android.os.Bundle
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.information_app.R
import com.example.information_app.ui.language_button.LanguageButtonViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var navController: NavController
//    private val languageButtonViewModel: LanguageButtonViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // always run on light mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        toolbar = findViewById(R.id.toolbar)

        // get NavController
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.findNavController()

        // set up toolbar
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController)
    }

    // set up navigate back button on action bar
    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp() || navController.navigateUp()
    }

    override fun onResume() {
        super.onResume()

// TODO language back to english on cold start
//        syncLocale()

        if (languageChanged) {
            showLanguagePopup()
            languageChanged = false
        }
    }

    private fun showLanguagePopup() {
        val language = getString(R.string.language)
        android.os.Handler(Looper.getMainLooper()).postDelayed({
            Snackbar.make(
                findViewById(android.R.id.content),
                "Language changed to $language",
                Snackbar.LENGTH_SHORT
            ).show()
        }, 500)
    }

    // accessible property over project
    companion object AppState {
        var languageChanged: Boolean = false
    }
}