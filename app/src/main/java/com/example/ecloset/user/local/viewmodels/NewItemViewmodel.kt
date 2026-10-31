package com.example.ecloset.user.local.viewmodels

import androidx.lifecycle.ViewModel
import com.example.ecloset.user.data.SessionManager
import com.example.ecloset.user.data.repo.ClothesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewItemViewmodel @Inject constructor(
	private val clothesRepo: ClothesRepository,
	private val sessionManager: SessionManager
): ViewModel() {
	private val currentUserId = sessionManager.currentUserId() ?: error("NewItemViewModel requires an authenticated session")
}