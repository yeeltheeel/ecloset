package com.example.ecloset.user.local.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecloset.user.data.SessionManager
import com.example.ecloset.user.data.repo.ClothesRepository
import com.example.ecloset.user.data.repo.OutfitsRepository
import com.example.ecloset.user.local.ClosetItemData
import com.example.ecloset.user.local.PreviewListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NewOutfitViewmodel @Inject constructor(
	private  val clothesRepo: ClothesRepository,
	private val outfitsRepo: OutfitsRepository,
	private val sessionManager: SessionManager
): ViewModel() {
	private val currentUserId = sessionManager.currentUserId() ?: error("NewOutfitViewModel requires an authenticated session")

	val clothesList: StateFlow<PreviewListState<ClosetItemData>> = clothesRepo.getAllClothes().toPreviewState()

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