package com.example.ecloset.user.data

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
	val color: Int,
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
	val items: List<ClosetItemData> = emptyList(),
	val isLoading: Boolean = false,
	val error: Int? = null
)

class ItemViewModel : ViewModel() {
	private val itemStateFlow = MutableStateFlow(ClosetItemState())
	val itemState: StateFlow<ClosetItemState> = itemStateFlow.asStateFlow()
}

// outfits
data class OutfitItemState(
	val outfits: List<OutfitItemData> = emptyList(),
	val isLoading: Boolean = false,
	val error: Int? = null
)

class OutfitViewModel : ViewModel() {
	private val outfitStateFlow = MutableStateFlow(OutfitItemState())
	val outfitState: StateFlow<OutfitItemState> = outfitStateFlow.asStateFlow()
}

sealed interface ClosetError {
	data class Resource(
		val stringRes: Int
	) : ClosetError
	data class Message(
		val text: String
	) : ClosetError
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
