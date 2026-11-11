package com.example.ecloset.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.ecloset.user.data.Presets.luminanceOptionsEnum as Luminance

@Composable
fun EClosetTheme(
	tint: Tint = Tint.Violet,
	dynamicColor: Boolean = false, // Dynamic color is available on Android 12+
	lumiPreference: Luminance = Luminance.SYSTEM,
	content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val isDark = when (lumiPreference) {
        Luminance.LIGHT -> false // force light
        Luminance.DARK -> true // force dark
        else -> isSystemInDarkTheme() // auto
    }

    val tintActual = when {
        (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) -> {
            val platform = if (isDark)
                dynamicDarkColorScheme(context)
            else
                dynamicLightColorScheme(context)
            Tint(platform.primary) // dynamic
        } else -> {
            tint // static
        }
    }

    val colorScheme = remember(isDark, tint, dynamicColor) {
        buildColorScheme(
            tint = tintActual,
            isDark = isDark
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
        shapes = Shapes(
            small = RoundedCornerShape(16.dp)
        )
    )
}
