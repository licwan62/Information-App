package com.example.information_app.ui.escort

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.information_app.R
import com.example.information_app.databinding.FragmentEscortingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EscortingFragment : Fragment(R.layout.fragment_escorting) {
    private lateinit var binding: FragmentEscortingBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentEscortingBinding.bind(view)
        binding.apply {
            buttonQuiz.setOnClickListener {
                val action = EscortingFragmentDirections.actionEscortingFragmentToQuizFragment(1)
                findNavController().navigate(action)
            }
            textViewParagraph.text =
                HtmlCompat.fromHtml(
                    getString(R.string.escorting_text),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            textViewParagraph.movementMethod =
                LinkMovementMethod.getInstance()
        }
    }
}