package com.projectasha.mobile.ui.common

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

object LocaleHelper {
    fun applyLanguage(tag: String) {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(tag))
    }

    fun currentLanguageTag(): String {
        val locales = AppCompatDelegate.getApplicationLocales()
        return locales.get(0)?.toLanguageTag() ?: "en"
    }
}
