package com.example.ecloset.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

/*
	tinting theme:
		grayscale for luminescence
		user set tint (presets or dynamic) for hue
*/

object GrayRamp {
	val N0   = Color(0xFF000000)
	val N5   = Color(0xFF0D0D0D)
	val N10  = Color(0xFF1A1A1A)
	val N15  = Color(0xFF262626)
	val N20  = Color(0xFF333333)
	val N25  = Color(0xFF404040)
	val N30  = Color(0xFF4D4D4D)
	val N35  = Color(0xFF595959)
	val N40  = Color(0xFF666666)
	val N45  = Color(0xFF737373)
	val N50  = Color(0xFF808080)
	val N55  = Color(0xFF8C8C8C)
	val N60  = Color(0xFF999999)
	val N65  = Color(0xFFA6A6A6)
	val N70  = Color(0xFFB3B3B3)
	val N75  = Color(0xFFBFBFBF)
	val N80  = Color(0xFFCCCCCC)
	val N85  = Color(0xFFD9D9D9)
	val N90  = Color(0xFFE6E6E6)
	val N95  = Color(0xFFF2F2F2)
	val N98  = Color(0xFFFAFAFA)
	val N100 = Color(0xFFFFFFFF)
}

val ErrorLight            = Color(0xFF8C1D18)
val OnErrorLight          = Color(0xFFFFFFFF)
val ErrorContainerLight   = Color(0xFF591612)
val OnErrorContainerLight = Color(0xFFF5D6D4)

val ErrorDark             = Color(0xFFE8A0A0)
val OnErrorDark           = Color(0xFF4A0F0C)
val ErrorContainerDark    = Color(0xFF600F0C)
val OnErrorContainerDark  = Color(0xFFF2B8B5)

data class Tint(
	val hue: Color,
	val chroma: Float = 0.20f,
) {
	companion object {
		val None   = Tint(GrayRamp.N50, chroma = 0f)
		val Violet   = Tint(Color(0xFF654381))
		val Sage   = Tint(Color(0xFF4C7C55))
		val Amber  = Tint(Color(0xFFD28E38))
		val Indigo = Tint(Color(0xFF2F65B4))
		val Rose   = Tint(Color(0xFFAF3E78))

		val presets = listOf(Violet, Sage, Amber, Indigo, Rose)
	}
}

private fun Color.toHsl(): FloatArray {
	val r = red; val g = green; val b = blue
	val max = maxOf(r, g, b)
	val min = minOf(r, g, b)
	val l = (max + min) / 2f
	val d = max - min

	val h = if (d == 0f) 0f else {
		when (max) {
			r -> (60f * ((g - b) / d) + 360f) % 360f
			g -> (60f * ((b - r) / d) + 120f) % 360f
			else -> (60f * ((r - g) / d) + 240f) % 360f
		}
	}
	val s = if (d == 0f) 0f else d / (1f - kotlin.math.abs(2f * l - 1f))
	return floatArrayOf(h, s, l)
}

private fun hslToColor(h: Float, s: Float, l: Float): Color {
	val c = (1f - kotlin.math.abs(2f * l - 1f)) * s
	val x = c * (1f - kotlin.math.abs((h / 60f) % 2f - 1f))
	val m = l - c / 2f

	val (r1, g1, b1) = when {
		h < 60f  -> Triple(c, x, 0f)
		h < 120f -> Triple(x, c, 0f)
		h < 180f -> Triple(0f, c, x)
		h < 240f -> Triple(0f, x, c)
		h < 300f -> Triple(x, 0f, c)
		else     -> Triple(c, 0f, x)
	}

	return Color(r1 + m, g1 + m, b1 + m, 1f)
}

fun tintGray(gray: Color, tint: Tint): Color {
	if (tint.chroma <= 0f)
		return gray

	val (grayH, _, grayL) = gray.toHsl().let { Triple(it[0], it[1], it[2]) }
	val tintHsl = tint.hue.toHsl()

	return hslToColor(
		h = tintHsl[0],
		s = tint.chroma,
		l = grayL,
	)
}
