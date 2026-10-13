package com.example.ecloset.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
fun EClosetTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    tint: Tint = Tint.Plum,
    dynamicColor: Boolean = false, // Dynamic color is available on Android 12+
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current

    val colorScheme = remember(darkTheme, tint, dynamicColor) {
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
                if (darkTheme)
                    dynamicDarkColorScheme(context)
                else
                    dynamicLightColorScheme(context)

            else -> buildColorScheme(
                tint = tint,
                isDark = darkTheme)
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
