package com.example.information_app.ui.caregivers

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.information_app.R
import com.example.information_app.databinding.FragmentCaregiversBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CaregiverFragment : Fragment(R.layout.fragment_caregivers) {

    private lateinit var binding: FragmentCaregiversBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCaregiversBinding.bind(view)
        binding.apply {

        }
    }

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
}