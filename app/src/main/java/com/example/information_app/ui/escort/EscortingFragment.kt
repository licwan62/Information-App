package com.example.information_app.ui.escort

import android.os.Bundle
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.information_app.R
import com.example.information_app.databinding.FragmentEscortingBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

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
                    getString(R.string.escorting_paragraph),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )

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