package com.example.information_app.ui.infomation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.example.information_app.R
import com.example.information_app.databinding.FragmentInformationBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

private val languages = arrayOf("es", "rar")
private var languageIdx = 0

@AndroidEntryPoint
class InformationFragment : Fragment(R.layout.fragment_information) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentInformationBinding.bind(view)
        binding.apply {
            buttonLanguage.setOnClickListener {
                languageIdx += 1
                languageIdx %= languages.count()
                Log.i("inf", "idx: $languageIdx")
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
    }
}