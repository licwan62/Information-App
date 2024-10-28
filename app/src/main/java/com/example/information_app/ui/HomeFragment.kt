package com.example.information_app.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.information_app.R

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var button_information: Button
    private lateinit var button_escorting: Button
    private lateinit var button_parents: Button
    private lateinit var button_caregivers: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_information = view.findViewById(R.id.button_information)
        button_escorting = view.findViewById(R.id.button_escorting)
        button_parents = view.findViewById(R.id.button_parents)
        button_caregivers = view.findViewById(R.id.button_caregivers)

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