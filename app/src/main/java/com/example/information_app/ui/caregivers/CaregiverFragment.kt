package com.example.information_app.ui.caregivers

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.core.text.HtmlCompat
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
            textViewParagraph.text =
                HtmlCompat.fromHtml(
                    getString(R.string.caregivers_text),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            textViewParagraph.movementMethod =
                LinkMovementMethod.getInstance()
        }
    }
}