package com.example.gmfastfood

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.core.os.LocaleListCompat

class LanguageManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_APP_LANGUAGE = "app_language"
        private const val DEFAULT_LANGUAGE = "en" // Default to English
    }

    fun getLanguage(): String {
        return sharedPreferences.getString(KEY_APP_LANGUAGE, DEFAULT_LANGUAGE) ?: DEFAULT_LANGUAGE
    }

    fun saveLanguage(languageCode: String) {
        sharedPreferences.edit { putString(KEY_APP_LANGUAGE, languageCode) }
    }
}


// Helper function to trigger the language change
  fun changeLanguage(languageTag: String) {
    val localeList = LocaleListCompat.forLanguageTags(languageTag)
    AppCompatDelegate.setApplicationLocales(localeList)
}

fun getCurrentLanguage(): String {
    return AppCompatDelegate.getApplicationLocales()[0]?.language ?: "en"
}