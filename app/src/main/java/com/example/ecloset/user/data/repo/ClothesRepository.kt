package com.example.ecloset.user.data.repo

import com.example.ecloset.user.data.ConnectionMode
import com.example.ecloset.user.data.ConnectionModeManager
import com.example.ecloset.user.local.ClosetItemData
import com.example.ecloset.user.local.datasource.LocalDataSource
import com.example.ecloset.user.remote.datasource.ClothesRemoteDataSource

class ClothesRepository(
	private val remote: ClothesRemoteDataSource,
	private val local: LocalDataSource,
	private val modeManager: ConnectionModeManager
) {
	suspend fun getClothes(userId: Int): List<ClosetItemData> =
		when (modeManager.current()) {
			ConnectionMode.FORCE_MOCK   -> local.getClothes()
			ConnectionMode.FORCE_REMOTE -> remote.getClothes(userId).getOrThrow()
			ConnectionMode.AUTO         -> remote.getClothes(userId).getOrNull() ?: local.getClothes()
		}
}
