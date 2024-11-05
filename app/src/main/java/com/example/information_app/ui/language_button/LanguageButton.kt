package com.example.information_app.ui.language_button

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.information_app.data.LanguageCode
import com.example.information_app.ui.util.exhaustive
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay

class LanguageButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : MaterialButton(context, attrs, defStyle) {

    private lateinit var viewModel: LanguageButtonViewModel

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
                        popupMessage(viewModel.languageCode.value, activity)

                        // Recreate the activity
                        restartActivity(activity)
                    }
                }.exhaustive
            }
        }
    }

    private fun popupMessage(newLanguageCode: LanguageCode?, activity: Activity) {
        if (newLanguageCode == null) {
            Log.e("LanguageButton", "null language code")
            return
        }

        Snackbar.make(
            activity.findViewById(android.R.id.content),
            "Language changed to ${newLanguageCode.name}",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private suspend fun restartActivity(activity: Activity) {
        delay(1000)
        activity.finish()

        val intent = activity.intent
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

        // Add a small delay to allow the system to properly process the finish
        Handler(Looper.getMainLooper()).postDelayed({
            activity.startActivity(intent)
            Log.i("LanguageButton", "on activity restart")
        }, 50) // Delay in milliseconds (e.g., 50ms)
    }
}