package com.example.ecloset.user.local.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecloset.R
import com.example.ecloset.user.data.repo.ClothesRepository
import com.example.ecloset.user.local.ClosetError
import com.example.ecloset.user.local.ClosetItemData
import com.example.ecloset.user.local.ClosetItemState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClosetItemViewModel @Inject constructor(
	private val clothesRepo: ClothesRepository
): ViewModel() {
	private val itemStateFlow = MutableStateFlow(ClosetItemState())
	val itemState: StateFlow<ClosetItemState> = itemStateFlow.asStateFlow()

	private val _closetError = MutableStateFlow<ClosetError?>(null)
	val closetError: StateFlow<ClosetError?> = _closetError.asStateFlow()

	private val currentUserId = 1 // replace with actual user.getId()

	init {
		loadAll()
	}

	fun loadAll(){
		viewModelScope.launch {
			itemStateFlow.update {
				it.copy(isLoading = true, error = null)
			}

			try {
				val items = clothesRepo.getClothes(currentUserId)
				itemStateFlow.update {
					it.copy(items = items, isLoading = false)
				}

				if (items.isEmpty()) {
					itemStateFlow.update {
						it.copy(error = R.string.error_generic)
					}
				}
			} catch (e: Exception) {
				itemStateFlow.update { 
					it.copy(isLoading = false)
				}
				_closetError.value = ClosetError.Resource(
					R.string.error_generic
				)
			}
		}
	}

	fun addNewItem(item: ClosetItemData){
		viewModelScope.launch {
			itemStateFlow.update {
				it.copy(isLoading = true, error = null)
			}

			clothesRepo.addItem(item).fold(
				onSuccess = { loadAll() },
				onFailure = {
					itemStateFlow.update {
						it.copy(isLoading = false)
					}
					_closetError.value = ClosetError.Resource(
						R.string.error_generic
					)
				}
			)
		}
	}

	fun updateItem(id: Int, data: ClosetItemData){
		viewModelScope.launch {
			itemStateFlow.update {
				it.copy(isLoading = true, error = null)
			}

			clothesRepo.updateItem(id, data).fold(
				onSuccess = { loadAll() },
				onFailure = {
					itemStateFlow.update {
						it.copy(isLoading = false)
					}
					_closetError.value = ClosetError.Resource(
						R.string.error_generic
					)
				}
			)
		}
	}

	fun removeItem(id: Int){
		viewModelScope.launch {
			itemStateFlow.update {
				it.copy(isLoading = true, error = null)
			}

			clothesRepo.removeItem(id).fold(
				onSuccess = { loadAll() },
				onFailure = {
					itemStateFlow.update {
						it.copy(isLoading = false)
					}
					_closetError.value = ClosetError.Resource(
						R.string.error_generic
					)
				}
			)
		}
	}

	fun clearResult() {
		_closetError.value = null
	}
}
