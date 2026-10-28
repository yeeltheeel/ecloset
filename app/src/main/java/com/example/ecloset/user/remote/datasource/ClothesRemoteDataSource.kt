package com.example.ecloset.user.remote.datasource

import com.example.ecloset.user.local.ClosetItemData
import com.example.ecloset.user.remote.api.ClothesApi
import com.example.ecloset.user.remote.dto.toUi

class ClothesRemoteDataSource(
	private val api: ClothesApi
) {
	suspend fun getClothes(
		userId: Int
	): Result<List<ClosetItemData>> = runCatching {
		api.getClothes(userId).map { it.toUi() }
	}
}
