package com.example.ecloset.user.local.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecloset.user.data.repo.ClothesRepository
import com.example.ecloset.user.local.ClosetItemData
import com.example.ecloset.user.local.ClosetItemState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClosetItemViewModel @Inject constructor(
	private val clothesRepo: ClothesRepository
): ViewModel() {
	private val itemStateFlow = MutableStateFlow(ClosetItemState())
	val itemState: StateFlow<ClosetItemState> = itemStateFlow.asStateFlow()

	init {
		loadAll()
	}

	fun loadAll(){
		viewModelScope.launch {}
	}

	fun addNewItem(item: ClosetItemData){ }

	fun updateItem(id: Int, data: ClosetItemData){ }

	fun removeItem(id: Int){ }
}