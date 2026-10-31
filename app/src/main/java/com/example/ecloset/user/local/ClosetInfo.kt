package com.example.ecloset.user.local

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

data class PreviewListState<out T>(
	val items: List<T> = emptyList(),
	val isLoading: Boolean = false,
	val error: Throwable? = null
)

data class CategoryItem(
	val id: Int,
	val titleId: Int
)

data class ClosetItemData(
	override val id: Int,
	override val title: String,
	override val imageUrl: String?,
	val category: Int, // same as CategoryItem.id
	val season: String, // or Int if we go with season_id
	val occasion: String,
	val color: Int, // id in colorList
	val material: String,
	override val dateAdded: LocalDate = LocalDate.now()
) : PreviewCardData

data class OutfitItemData(
	override val id: Int,
	override val title: String,
	override val imageUrl: String?,
	val season: String, // or Int if we go with season_id
	val occasion: String,
	val color: Int, // id in colorList
	override val dateAdded: LocalDate = LocalDate.now()
) : PreviewCardData

interface PreviewCardData {
	val id: Int
	val title: String
	val imageUrl: String?
	val dateAdded: LocalDate
}

// clothes
data class ClosetItemState(
	val items: List<ClosetItemData> = emptyList(),
	val isLoading: Boolean = false,
	val error: Int? = null
)

// outfits
data class OutfitItemState(
	val outfits: List<OutfitItemData> = emptyList(),
	val isLoading: Boolean = false,
	val error: Int? = null
)

sealed interface ClosetError {
	data class Resource(
		val stringRes: Int
	) : ClosetError
	data class Message(
		val text: String
	) : ClosetError
}

