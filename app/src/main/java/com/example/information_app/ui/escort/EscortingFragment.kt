package com.example.information_app.ui.escort

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.information_app.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EscortingFragment : Fragment(R.layout.fragment_escorting) {

    private lateinit var button_quiz: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_quiz = view.findViewById(R.id.button_quiz)
        button_quiz.setOnClickListener {
            val action = EscortingFragmentDirections.actionEscortingFragmentToQuizFragment(1)
            findNavController().navigate(action)
        }

    }

}