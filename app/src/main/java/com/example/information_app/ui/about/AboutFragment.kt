package com.example.information_app.ui.about

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.example.information_app.R
import com.example.information_app.databinding.FragmentAboutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutFragment : Fragment(R.layout.fragment_about) {
    private lateinit var binding: FragmentAboutBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAboutBinding.bind(view)
        binding.apply {
            textViewParagraph.text =
                HtmlCompat.fromHtml(
                    getString(R.string.about_document),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )

            // enable link click event to its href
            textViewParagraph.movementMethod =
                LinkMovementMethod.getInstance()

//            webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
        }
    }
}