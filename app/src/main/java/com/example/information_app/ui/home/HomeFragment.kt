package com.example.information_app.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.information_app.R
import com.example.information_app.databinding.FragmentHomeBinding
import com.example.information_app.ui.language_button.LanguageButtonViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private val languageButtonViewModel: LanguageButtonViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)

        binding.apply {
            buttonLanguage.bind(languageButtonViewModel, viewLifecycleOwner, requireActivity())

            buttonLanguage.setOnClickListener {
                languageButtonViewModel.updateLanguageCode(requireActivity())
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

            buttonCustodians.setOnClickListener {
                val action = HomeFragmentDirections
                    .actionHomeFragmentToCustodiansFragment()
                findNavController().navigate(action)
            }
        }
    }
}