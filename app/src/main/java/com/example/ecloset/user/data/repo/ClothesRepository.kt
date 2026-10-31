package com.example.ecloset.user.data.repo

import com.example.ecloset.user.data.ConnectionMode
import com.example.ecloset.user.data.ConnectionModeManager
import com.example.ecloset.user.local.ClosetItemData
import com.example.ecloset.user.local.datasource.LocalDataSource
import com.example.ecloset.user.remote.datasource.ClothesRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlin.collections.emptyList

class ClothesRepository(
	private val remote: ClothesRemoteDataSource,
	private val local: LocalDataSource,
	private val modeManager: ConnectionModeManager
) {
	private var cachedItems: List<ClosetItemData> = local.getClothes()
	private val cachedItemsFlow = MutableStateFlow<List<ClosetItemData>>(cachedItems)
	private val unusedItemsFlow = MutableStateFlow<List<ClosetItemData>>(cachedItems)

	suspend fun getClothes(userId: Int): List<ClosetItemData> {
		val result = when (modeManager.current()) {
			ConnectionMode.FORCE_MOCK -> local.getClothes()
			ConnectionMode.FORCE_REMOTE -> remote.getClothes(userId).getOrThrow()
			ConnectionMode.AUTO -> remote.getClothes(userId).getOrNull() ?: local.getClothes()
		}
		cachedItems = result
		return result
	}

	fun getPreview(userId: Int, limit: Int): Flow<List<ClosetItemData>> {
		val preview = cachedItemsFlow.map { items ->
			items.take(limit)
		}
		return preview
	}

	fun getAllClothes(): Flow<List<ClosetItemData>> = cachedItemsFlow.asStateFlow()

	fun getUnused(userId: Int, limit: Int): Flow<List<ClosetItemData>> {
		val unused = flowOf(emptyList<ClosetItemData>())
		return unused
	}

	suspend fun addItem(item: ClosetItemData): Result<Unit> {
		// TODO: Replace with remote.addItem() when endpoint is ready
		cachedItems = cachedItems + item
		return Result.success(Unit)
	}

	suspend fun updateItem(id: Int, data: ClosetItemData): Result<Unit> {
		// TODO: Replace with remote.updateItem() when endpoint is ready
		val index = cachedItems.indexOfFirst { it.id == id }
		if (index == -1) return Result.failure(Exception("Item not found"))

		cachedItems = cachedItems.toMutableList().apply { this[index] = data }
		return Result.success(Unit)
	}

	suspend fun removeItem(id: Int): Result<Unit> {
		// TODO: Replace with remote.removeItem() when endpoint is ready
		cachedItems = cachedItems.filterNot { it.id == id }
		return Result.success(Unit)
	}
}
