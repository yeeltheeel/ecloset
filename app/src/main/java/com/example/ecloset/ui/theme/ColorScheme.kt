package com.example.ecloset.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

fun buildColorScheme(tint: Tint, isDark: Boolean) = with(GrayRamp) {
	fun t(c: Color) = tintGray(c, tint)

	if (isDark) {
		darkColorScheme(
			/* --- primary: light in dark mode (actions pop against dark surfaces) --- */
			primary            = t(N80),
			onPrimary          = N20,
			primaryContainer   = t(N35),
			onPrimaryContainer = N90,

			secondary            = t(N65),
			onSecondary          = N10,
			secondaryContainer   = t(N40),
			onSecondaryContainer = N90,

			tertiary            = t(N50),
			onTertiary          = N90,
			tertiaryContainer   = t(N20),
			onTertiaryContainer = N90,

			/* --- surface hierarchy: header < page < cards --- */
			background          = t(N10),
			surface             = t(N15),
			surfaceVariant      = t(N20),

			/* --- text --- */
			onBackground        = N90,
			onSurface           = N90,
			onSurfaceVariant    = N80,

			outline             = t(N50),
			outlineVariant      = t(N35),

			error               = ErrorDark,
			onError             = OnErrorDark,
			errorContainer      = ErrorContainerDark,
			onErrorContainer    = OnErrorContainerDark,
		)
	} else {
		lightColorScheme(
			/* --- primary: dark in light mode --- */
			primary            = t(N35),
			onPrimary          = N98,
			primaryContainer   = t(N90),
			onPrimaryContainer = N20,

			secondary            = t(N50),
			onSecondary          = N98,
			secondaryContainer   = t(N90),
			onSecondaryContainer = N40,

			tertiary            = t(N65),
			onTertiary          = N98,
			tertiaryContainer   = t(N90),
			onTertiaryContainer = N20,

			/* --- surface hierarchy: header < page < cards --- */
			background          = t(N98),
			surface             = t(N90),
			surfaceVariant      = N100,

			/* --- text --- */
			onBackground        = N20,
			onSurface           = N20,
			onSurfaceVariant    = N35,

			outline             = t(N65),
			outlineVariant      = t(N80),

			error               = ErrorLight,
			onError             = OnErrorLight,
			errorContainer      = ErrorContainerLight,
			onErrorContainer    = OnErrorContainerLight,
		)
	}
}