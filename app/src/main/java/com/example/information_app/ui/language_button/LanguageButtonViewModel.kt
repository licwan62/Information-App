package com.example.information_app.ui.language_button

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.information_app.data.LanguageCode
import com.example.information_app.data.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class LanguageButtonViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    // keep track on preference storing language code
    var languageCode: LiveData<LanguageCode> =
        preferenceManager.languageCodeFlow.asLiveData()

    fun updateLanguageCode(activity: Activity) = viewModelScope.launch {
        // get the other language in enum
        val newLanguageCode = when (languageCode.value) {
            LanguageCode.ENG -> LanguageCode.MAO
            else -> LanguageCode.ENG
        }
        setLocale(newLanguageCode.name, activity)
        currentLanguageCode = newLanguageCode.name
        preferenceManager.updateLanguageCode(newLanguageCode)
    }

    private fun setLocale(languageCode: String, activity: Activity) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = activity.resources.configuration
        config.setLocale(locale)

        //activity.createConfigurationContext(config)
        activity.resources.updateConfiguration(config, activity.resources.displayMetrics)

        Log.i("LanguageButton", "on setLocale to: $languageCode")

        displayNewLanguage()
    }

    private fun displayNewLanguage() = viewModelScope.launch {
        languageChangingChannel.send(LanguageChangingAction.LanguageChanged)
    }

    sealed class LanguageChangingAction {
        object LanguageChanged : LanguageChangingAction()
    }

    private val languageChangingChannel = Channel<LanguageChangingAction>()
    val languageChangingFlow = languageChangingChannel.receiveAsFlow()

    companion object Language{
        lateinit var currentLanguageCode: String
    }
}