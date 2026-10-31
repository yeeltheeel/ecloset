package com.example.ecloset.user.local.viewmodels

import androidx.lifecycle.ViewModel
import com.example.ecloset.user.data.SessionManager
import com.example.ecloset.user.data.repo.ClothesRepository
import com.example.ecloset.user.data.repo.OutfitsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewOutfitViewmodel @Inject constructor(
	private val outfitsRepo: OutfitsRepository,
	private val sessionManager: SessionManager
): ViewModel() {
	private val currentUserId = sessionManager.currentUserId() ?: error("NewOutfitViewModel requires an authenticated session")
}