package com.example.information_app.data

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

enum class LanguageCode { ENG, MI }

/*// single object holding multiple values to be emit from preferenceFlow
data class FilterPreferences(val languageCode: LanguageCode)*/

//val Context.dataStore by preferencesDataStore(name = "user_preferences")

@Singleton
class PreferenceManager @Inject constructor(@ApplicationContext context: Context) {

    // hold preferences, emit on changes
    private val dataStore = context.createDataStore("user_preferences")

    /* language code of type enum item stored as string inside preference */

    // input language code enum item into preferences
    suspend fun updateLanguageCode(languageCode: LanguageCode) {
        dataStore.edit { preferences ->
            // persist a string at key: LANGUAGE_CODE
            preferences[LANGUAGE_CODE] = languageCode.name
//            Log.d("LanguageButton", "input ${languageCode.name} into preference")
        }
    }

    // Observed property that hold emission of data stored in preference
    val languageCodeFlow: Flow<LanguageCode> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e("PreferenceManager", "Error reading languageCode", exception)
                emit(emptyPreferences())
            } else throw exception
        }
        .map { preferences ->
            // emit data for LANGUAGE_CODE, and convert it into enum item
            val data = preferences[LANGUAGE_CODE] ?: LanguageCode.ENG.name
            val languageCode = LanguageCode.valueOf(data)
            Log.d("LanguageButton", "preference emit code: ${languageCode.name}")
            languageCode
        }

    companion object PreferencesKeys {
        // key of a string data of language code
        val LANGUAGE_CODE = preferencesKey<String>("language_code")
    }
}