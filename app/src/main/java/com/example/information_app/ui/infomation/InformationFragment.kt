package com.example.information_app.ui.infomation

import android.os.Bundle
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.example.information_app.R
import com.example.information_app.databinding.FragmentInformationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InformationFragment : Fragment(R.layout.fragment_information) {
    private lateinit var binding: FragmentInformationBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentInformationBinding.bind(view)
        binding.apply {
            textViewParagraph.text =
                HtmlCompat.fromHtml(
                    getString(R.string.about_text),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
        }
    }
}