package com.example.information_app.ui.custodians

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.information_app.R
import com.example.information_app.databinding.FragmentCaregiversBinding
import com.example.information_app.databinding.FragmentCustodiansBinding
import com.example.information_app.ui.languageIdx
import com.example.information_app.ui.languages
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CustodiansFragment : Fragment(R.layout.fragment_custodians) {
    private lateinit var binding: FragmentCustodiansBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCustodiansBinding.bind(view)
        binding.apply {
            buttonLanguage.setOnClickListener {
                languageIdx += 1
                languageIdx %= languages.count()
                setLocale(languages[languageIdx])
            }
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