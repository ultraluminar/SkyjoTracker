package com.example.skyjotracker.ui

import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

@Composable
fun LocaleContextWrapper(locale: Locale, content: @Composable () -> Unit) {
    val currentContext = LocalContext.current
    val localizedContext = currentContext.createLocaleContext(locale)

    CompositionLocalProvider(LocalContext provides localizedContext) { content() }
}

private fun Context.createLocaleContext(locale: Locale): Context {
    val config = Configuration(resources.configuration)
    config.setLocale(locale)
    return createConfigurationContext(config)
}
