package com.example.ecloset.user.data

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.sql.Date

// data types
data class CategoryItem(
	val id: Int,
	val titleId: Int
)

data class ClosetItemData(
	override val id: Int,
	override val title: String,
	override val type: String,
	override val imageUrl: String?,
	val category: String, // same as CategoryItem
	val season: String,
	val material: String,
	val color: Color,
	val occasion: String,
	override val dateAdded: Date
) : PreviewCardData

data class OutfitItemData(
	override val id: Int,
	override val title: String,
	override val type: String,
	override val imageUrl: String?,
	val season: String, // or Int if we go with season_id
	val occasion: String,
	override val dateAdded: Date
) : PreviewCardData

interface PreviewCardData {
	val id: Int
	val title: String
	val type: String
	val imageUrl: String?
	val dateAdded: Date
}

// clothes
data class ClosetItemState(
	val items: Array<ClosetItemData>,
	val isLoading: Boolean = false,
	val error: Int? = null
) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as ClosetItemState

		if (isLoading != other.isLoading) return false
		if (error != other.error) return false
		if (!items.contentEquals(other.items)) return false

		return true
	}

	override fun hashCode(): Int {
		var result = isLoading.hashCode()
		result = 31 * result + error.hashCode()
		result = 31 * result + items.contentHashCode()
		return result
	}
}

class ItemViewModel : ViewModel() {
	// tbd
}

// outfits
data class OutfitItemState(
	val items: Array<OutfitItemData>,
	val isLoading: Boolean = false,
	val error: Int? = null
) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as OutfitItemState

		if (isLoading != other.isLoading) return false
		if (error != other.error) return false
		if (!items.contentEquals(other.items)) return false

		return true
	}

	override fun hashCode(): Int {
		var result = isLoading.hashCode()
		result = 31 * result + error.hashCode()
		result = 31 * result + items.contentHashCode()
		return result
	}
}

class OutfitViewModel : ViewModel() {
	// tbd
}

// for preview
data class PreviewCard(
	// wrapper for PreviewCardData ???
	val id: Int,
	val title: String,
	val type: String,
	val imageUrl: String? = null,
	val dateAdded: Date = Date(System.currentTimeMillis())
)
