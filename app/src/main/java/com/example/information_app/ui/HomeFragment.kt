package com.example.information_app.ui

import android.content.res.Configuration
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.information_app.R
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var button_language: Button
    private lateinit var button_information: Button
    private lateinit var button_escorting: Button
    private lateinit var button_parents: Button
    private lateinit var button_caregivers: Button
    private val languages = arrayOf("English", "Maori")
    private var current_language = 0

    fun setLocal(languageCode: String) {
        val locale = Locale(languageCode)

        val config = Configuration(resources.configuration)
            .apply {
                setLocale(locale)
            }

        button_language.text = languageCode

        val msg = "language changed to " + languageCode
        Snackbar.make(requireView(), msg, Snackbar.LENGTH_LONG).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_language = view.findViewById(R.id.button_language)
        button_information = view.findViewById(R.id.button_information)
        button_escorting = view.findViewById(R.id.button_escorting)
        button_parents = view.findViewById(R.id.button_parents)
        button_caregivers = view.findViewById(R.id.button_caregivers)

        button_language.setOnClickListener {
            current_language = (current_language + 1) % 2
            val language_code = languages[current_language]
            setLocal(language_code)
        }

        button_information.setOnClickListener {
            var action = HomeFragmentDirections.actionHomeFragmentToInformationFragment()
            findNavController().navigate(action)
        }

        button_escorting.setOnClickListener {
            var action = HomeFragmentDirections.actionHomeFragmentToEscortingFragment()
            findNavController().navigate(action)
        }

        button_parents.setOnClickListener {
            var action = HomeFragmentDirections.actionHomeFragmentToParentFragment()
            findNavController().navigate(action)
        }

        button_caregivers.setOnClickListener {
            var action = HomeFragmentDirections.actionHomeFragmentToCaregiverFragment()
            findNavController().navigate(action)
        }
    }

}