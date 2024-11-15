package com.example.information_app.ui.custodians

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.example.information_app.R
import com.example.information_app.databinding.FragmentCaregiversBinding
import com.example.information_app.databinding.FragmentCustodiansBinding
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
            textViewParagraph.text =
                HtmlCompat.fromHtml(
                    getString(R.string.custodians_document),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            textViewParagraph.movementMethod =
                LinkMovementMethod.getInstance()
        }

    }
}