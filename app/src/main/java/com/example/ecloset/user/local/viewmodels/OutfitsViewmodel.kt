package com.example.ecloset.user.local.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecloset.R
import com.example.ecloset.user.data.SessionManager
import com.example.ecloset.user.data.repo.OutfitsRepository
import com.example.ecloset.user.local.ClosetError
import com.example.ecloset.user.local.OutfitItemData
import com.example.ecloset.user.local.OutfitItemState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OutfitViewModel @Inject constructor(
	private val outfitsRepo: OutfitsRepository,
	private val sessionManager: SessionManager
): ViewModel() {
	private val _outfitStateFlow = MutableStateFlow(OutfitItemState())
	val outfitState: StateFlow<OutfitItemState> = _outfitStateFlow.asStateFlow()

	private val _closetError = MutableStateFlow<ClosetError?>(null)
	val closetError: StateFlow<ClosetError?> = _closetError.asStateFlow()

	private val currentUserId = sessionManager.currentUserId()?: error("OutfitsViewModel requires an authenticated session")

	init {
		loadAll()
	}

	fun loadAll(){
		viewModelScope.launch {
			_outfitStateFlow.update {
				it.copy(isLoading = true, error = null)
			}

			try {
				val outfits = outfitsRepo.getOutfits(currentUserId)
				_outfitStateFlow.update {
					it.copy(outfits = outfits, isLoading = false)
				}

				if (outfits.isEmpty()) {
					_outfitStateFlow.update {
						it.copy(error = R.string.error_generic)
					}
				}
			} catch (e: Exception) {
				_outfitStateFlow.update {
					it.copy(isLoading = false)
				}
				_closetError.value = ClosetError.Resource(
					R.string.error_generic
				)
			}
		}
	}

	fun addNewOutfit(outfit: OutfitItemData){
		viewModelScope.launch {
			_outfitStateFlow.update {
				it.copy(isLoading = true, error = null)
			}

			outfitsRepo.addOutfit(outfit).fold(
				onSuccess = { loadAll() },
				onFailure = {
					_outfitStateFlow.update {
						it.copy(isLoading = false)
					}
					_closetError.value = ClosetError.Resource(
						R.string.error_generic
					)
				}
			)
		}
	}

	fun updateOutfit(id: Int, data: OutfitItemData){
		viewModelScope.launch {
			_outfitStateFlow.update {
				it.copy(isLoading = true, error = null)
			}

			outfitsRepo.updateOutfit(id, data).fold(
				onSuccess = { loadAll() },
				onFailure = {
					_outfitStateFlow.update {
						it.copy(isLoading = false)
					}
					_closetError.value = ClosetError.Resource(
						R.string.error_generic
					)
				}
			)
		}
	}

	fun removeOutfit(id: Int){
		viewModelScope.launch {
			_outfitStateFlow.update {
				it.copy(isLoading = true, error = null)
			}

			outfitsRepo.removeOutfit(id).fold(
				onSuccess = { loadAll() },
				onFailure = {
					_outfitStateFlow.update {
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
