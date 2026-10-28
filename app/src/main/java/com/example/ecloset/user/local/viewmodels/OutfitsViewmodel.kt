package com.example.ecloset.user.local.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecloset.user.data.repo.OutfitsRepository
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

	init {
		loadAll()
	}

	fun loadAll(){
		viewModelScope.launch {}
	}

	fun addNewOutfit(outfit: OutfitItemData){ }

	fun updateOutfit(id: Int, data: OutfitItemData){ }

	fun removeOutfit(id: Int){ }
}
