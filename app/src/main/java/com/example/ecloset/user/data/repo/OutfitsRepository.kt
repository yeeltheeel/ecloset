package com.example.ecloset.user.data.repo

import com.example.ecloset.user.data.ConnectionMode
import com.example.ecloset.user.data.ConnectionModeManager
import com.example.ecloset.user.local.ClosetItemData
import com.example.ecloset.user.local.OutfitItemData
import com.example.ecloset.user.local.datasource.LocalDataSource
import com.example.ecloset.user.remote.datasource.OutfitsRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class OutfitsRepository(
	private val remote: OutfitsRemoteDataSource,
	private val local: LocalDataSource,
	private val modeManager: ConnectionModeManager
) {
	private var cachedOutfits: List<OutfitItemData> = local.getOutfits()
	private val cachedOutfitsFlow = MutableStateFlow<List<OutfitItemData>>(cachedOutfits)

	suspend fun getOutfits(userId: Int): List<OutfitItemData> {
		val result = when (modeManager.current()) {
			ConnectionMode.FORCE_MOCK -> local.getOutfits()
			ConnectionMode.FORCE_REMOTE -> remote.getOutfits(userId).getOrThrow()
			ConnectionMode.AUTO -> remote.getOutfits(userId).getOrNull() ?: local.getOutfits()
		}
		cachedOutfits = result
		return result
	}

	fun getPreview(userId: Int, limit: Int): Flow<List<OutfitItemData>> {
		val preview = cachedOutfitsFlow.map { outfits ->
			outfits.take(limit)
		}
		return preview
	}

	suspend fun addOutfit(outfit: OutfitItemData): Result<Unit> {
		// TODO: Replace with remote.addItem() when endpoint is ready
		cachedOutfits = cachedOutfits + outfit
		return Result.success(Unit)
	}

	suspend fun updateOutfit(id: Int, data: OutfitItemData): Result<Unit> {
		// TODO: Replace with remote.updateItem() when endpoint is ready
		val index = cachedOutfits.indexOfFirst { it.id == id }
		if (index == -1)
			return Result.failure(Exception("Item not found"))

		cachedOutfits = cachedOutfits.toMutableList().apply { this[index] = data }
		return Result.success(Unit)
	}

	suspend fun removeOutfit(id: Int): Result<Unit> {
		// TODO: Replace with remote.removeItem() when endpoint is ready
		cachedOutfits = cachedOutfits.filterNot { it.id == id }
		return Result.success(Unit)
	}
}
