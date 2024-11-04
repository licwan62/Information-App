package com.example.information_app.ui.util

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.information_app.data.LanguageCode
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay

class LanguageButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : MaterialButton(context, attrs, defStyle) {

    private var viewModel: LanguageButtonViewModel? = null

    init {
        text = LanguageCode.EN.name
    }

    fun bind(
        viewModel: LanguageButtonViewModel,
        lifecycleOwner: LifecycleOwner,
        activity: Activity
    ) {
        this.viewModel = viewModel

        setOnClickListener {
            viewModel.updateLanguageCode(activity) // Or use the appropriate language code
        }

        viewModel.languageCode.observe(lifecycleOwner) { languageCode ->
            Log.d("LanguageButton", "on observe changes on languageCode: ${languageCode.name}")
            text = languageCode.name
        }

        lifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.languageChangingFlow.collect { event ->
                when (event) {
                    is LanguageButtonViewModel.LanguageChangingAction.LanguageChanged -> {

                        // Display the snack-bar
                        val newLanguageCode = viewModel.languageCode.value
                        if (newLanguageCode != null) {
                            popupSnackbar(newLanguageCode, activity as Activity)
                        } else {
                            Log.e("LanguageButton", "null language code")
                        }

                        delay(500)

                        // Recreate the activity
                        restartActivity(activity)
                    }
                }.exhaustive
            }
        }
    }

    private fun popupSnackbar(newLanguageCode: LanguageCode, activity: Activity) {
        Snackbar.make(
            activity.findViewById(android.R.id.content),
            "Language changed to ${newLanguageCode.name}",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun restartActivity(activity: Activity) {
        val intent = activity.intent
        activity.finish()
        activity.startActivity(intent)
        activity.overridePendingTransition(
            0,
            0
        ) // Optional: remove transition for smoother experience
        Log.i("LanguageButton", "on activity restart")
    }
}