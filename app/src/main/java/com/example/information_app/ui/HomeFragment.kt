package com.example.information_app.ui

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.information_app.R
import com.example.information_app.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

//private val languages = arrayOf("es", "rar")
//private var languageIdx = 0

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    lateinit var binding: FragmentHomeBinding

    private fun setLocale(languageCode: String) {
        val context = requireContext()
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = context.resources.configuration
        config.setLocale(locale)

        context.resources.updateConfiguration(
            config,
            context.resources.displayMetrics
        )
        binding.apply {
            activity?.recreate()
        }
        val msg = "language changed to $languageCode"
        Snackbar.make(requireView(), msg, Snackbar.LENGTH_LONG).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)
        binding.apply {
            buttonLanguage.setOnClickListener {
                languageIdx += 1
                languageIdx %= languages.count()
                setLocale(languages[languageIdx])
            }
            buttonCaregivers.setOnClickListener {
                val action = HomeFragmentDirections
                    .actionHomeFragmentToCaregiverFragment()
                findNavController().navigate(action)
            }
            buttonInformation.setOnClickListener {
                val action = HomeFragmentDirections
                    .actionHomeFragmentToInformationFragment()
                findNavController().navigate(action)
            }
            buttonEscorting.setOnClickListener {
                val action = HomeFragmentDirections
                    .actionHomeFragmentToEscortingFragment()
                findNavController().navigate(action)
            }
            buttonParents.setOnClickListener {
                val action = HomeFragmentDirections
                    .actionHomeFragmentToParentFragment()
                findNavController().navigate(action)
            }
        }
    }

}