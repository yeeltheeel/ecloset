package com.example.ecloset.user.remote.datasource

import com.example.ecloset.user.local.OutfitItemData
import com.example.ecloset.user.remote.api.OutfitsApi
import com.example.ecloset.user.remote.dto.toUi

class OutfitsRemoteDataSource(
	private val api: OutfitsApi
) {
	suspend fun getOutfits(
		userId: Int
	): Result<List<OutfitItemData>> = runCatching {
		api.getOutfits(userId).map { it.toUi() }
	}
}