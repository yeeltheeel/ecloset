package com.example.ecloset.user.local.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecloset.user.data.SessionManager
import com.example.ecloset.user.data.repo.ClothesRepository
import com.example.ecloset.user.local.ClosetItemData
import com.example.ecloset.user.local.ClosetItemState
import com.example.ecloset.user.local.PreviewListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NewItemViewmodel @Inject constructor(
	private val clothesRepo: ClothesRepository,
	private val sessionManager: SessionManager
): ViewModel() {
	private val currentUserId = sessionManager.currentUserId() ?: error("NewItemViewModel requires an authenticated session")

	val clothesList: StateFlow<PreviewListState<ClosetItemData>> = clothesRepo.getAllClothes().toPreviewState()
	private val _newItem = MutableStateFlow<ClosetItemData>(ClosetItemData(
		id = -1,
		title = "",
		imageUrl = null,
		category = -1,
		season = "",
		occasion = "",
		color = -1,
		material = ""
	))
	val newItem: StateFlow<ClosetItemData> = _newItem.asStateFlow()

	fun addNewItem(item: ClosetItemData){
		clothesRepo.addItem(item)
	}

	fun updateNewItem(newItemData: ClosetItemData){
		//
	}

	private fun <T> Flow<List<T>>.toPreviewState(): StateFlow<PreviewListState<T>> = map {
		PreviewListState(items = it, isLoading = false)
	}.onStart {
		emit(PreviewListState(isLoading = true))
	}.catch {
		emit(PreviewListState(error = it))
	}.stateIn(
		viewModelScope,
		SharingStarted.WhileSubscribed(5000),
		PreviewListState(isLoading = true)
	)
}