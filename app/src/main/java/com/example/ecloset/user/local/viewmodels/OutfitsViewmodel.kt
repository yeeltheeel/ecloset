package com.example.ecloset.user.local.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecloset.user.data.repo.OutfitsRepository
import com.example.ecloset.user.local.ClosetError
import com.example.ecloset.user.local.OutfitItemData
import com.example.ecloset.user.local.OutfitItemState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OutfitViewModel @Inject constructor(
	private val oufitsRepo: OutfitsRepository
): ViewModel() {
	private val outfitStateFlow = MutableStateFlow(OutfitItemState())
	val outfitState: StateFlow<OutfitItemState> = outfitStateFlow.asStateFlow()

	private val _closetError = MutableStateFlow<ClosetError?>(null)
	val closetError: StateFlow<ClosetError?> = _closetError.asStateFlow()

	init {
		loadAll()
	}

	fun loadAll(){
		viewModelScope.launch {}
	}

	fun addNewOutfit(outfit: OutfitItemData){ }

	fun updateOutfit(id: Int, data: OutfitItemData){ }

	fun removeOutfit(id: Int){ }

	fun clearResult() {
		_closetError.value = null
	}
}
