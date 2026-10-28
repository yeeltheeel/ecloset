package com.example.ecloset.user.data.repo

import com.example.ecloset.user.data.ConnectionMode
import com.example.ecloset.user.data.ConnectionModeManager
import com.example.ecloset.user.local.OutfitItemData
import com.example.ecloset.user.local.datasource.LocalDataSource
import com.example.ecloset.user.remote.datasource.OutfitsRemoteDataSource

class OutfitsRepository(
	private val remote: OutfitsRemoteDataSource,
	private val local: LocalDataSource,
	private val modeManager: ConnectionModeManager
) {
	suspend fun getOutfits(userId: Int): List<OutfitItemData> =
		when (modeManager.current()) {
			ConnectionMode.FORCE_MOCK   -> local.getOutfits()
			ConnectionMode.FORCE_REMOTE -> remote.getOutfits(userId).getOrThrow()
			ConnectionMode.AUTO         -> remote.getOutfits(userId).getOrNull() ?: local.getOutfits()
		}
}
