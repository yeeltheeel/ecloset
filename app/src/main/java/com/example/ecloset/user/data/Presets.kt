package com.example.ecloset.user.data

import androidx.compose.ui.graphics.Color
import com.example.ecloset.R
import com.example.ecloset.user.local.CategoryItem
import com.example.ecloset.user.local.LocationInfo

object Presets {
	// localize later (prob will be list<int>)
	val selectOptions = listOf("Static", "Dynamic")
	val luminenceOptions = listOf("Light", "Dark", "System")
	val locations = emptyList<LocationInfo>()
	val themePresets = listOf("None", "Violet", "Sage", "Amber", "Indigo", "Rose")

	enum class selectOptionsEnum {
		STATIC,
		DYNAMIC
	}

	enum class luminenceOptionsEnum {
		LIGHT,
		DARK,
		SYSTEM
	}

	enum class themePresetsEnum {
		NONE,
		VIOLET,
		SAGE,
		AMBER,
		INDIGO,
		ROSE
	}

	enum class categoriesEnum {
		NONE,
		TOP,
		BOTTOM,
		SHOES,
		CARDIGANS,
		COATS,
		ACCESSORIES
	}

	enum class colorsEnum {
		GREEN,
		BROWN,
		PURPLE,
		PINK,
		RED,
		YELLOW,
		BLUE,
		WHITE,
		BLACK,
		GRAY
	}

	data class ColorSelect(
		val id: Int,
		val name: String, // will become int when localized
		val value: Color
	)

	val colorList = listOf(
		ColorSelect(1, "Green", Color(0xFF107E19)),
		ColorSelect(2, "Brown", Color(0xFF443129)),
		ColorSelect(3, "Purple", Color(0xFF8E49FF)),
		ColorSelect(4, "Pink", Color(0xFFFF72FC)),
		ColorSelect(5, "Red", Color(0xFF6E0017)),
		ColorSelect(6, "Yellow", Color(0xFFE0AB00)),
		ColorSelect(7, "Blue", Color(0xFF184DA1)),
		ColorSelect(8, "White", Color(0xFFFFFFFF)),
		ColorSelect(9, "Black", Color(0xFF000000)),
		ColorSelect(10, "Gray", Color(0xFF6B6B6B))
	)

	val clothesCategories = listOf(
		CategoryItem(
			id = 1,
			titleId = R.string.items_tops
		),
		CategoryItem(
			id = 2,
			titleId = R.string.items_bottoms
		),
		CategoryItem(
			id = 3,
			titleId = R.string.items_shoes
		),
		CategoryItem(
			id = 4,
			titleId = R.string.items_cardigans
		),
		CategoryItem(
			id = 5,
			titleId = R.string.items_coats
		),
		CategoryItem(
			id = 6,
			titleId = R.string.items_accessories
		)
	)
}