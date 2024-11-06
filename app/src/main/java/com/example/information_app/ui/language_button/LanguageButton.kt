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
import com.example.information_app.R
import com.example.information_app.data.LanguageCode
import com.example.information_app.ui.MainActivity
import com.example.information_app.ui.util.exhaustive
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import java.lang.Thread.sleep
import kotlin.math.log

const val TAG = "LanguageButton"

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
            Log.d(TAG, "on observe changes on languageCode: ${languageCode.name}")
            text = languageCode.name
        }

        lifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.languageChangingFlow.collect { event ->
                when (event) {
                    is LanguageButtonViewModel.LanguageChangingAction.LanguageChanged -> {
                        // Recreate the activity
                        Handler(Looper.getMainLooper()).postDelayed({
                            restartActivity(activity)
                        }, 250)
                    }
                }.exhaustive
            }
        }
    }

    private fun restartActivity(activity: Activity) {
        val intent = activity.intent

        MainActivity.languageChanged = true
        Log.i(TAG, "language flag set to : ${MainActivity.languageChanged}")

        activity.finish()

        // Add a small delay to allow the system to properly process the finish
        Handler(Looper.getMainLooper()).postDelayed({
            activity.startActivity(intent)
        }, 50)
    }
}