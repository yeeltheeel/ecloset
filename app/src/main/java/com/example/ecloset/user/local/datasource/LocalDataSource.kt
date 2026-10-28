package com.example.ecloset.user.local.datasource

import com.example.ecloset.user.data.MockData
import com.example.ecloset.user.local.ClosetItemData
import com.example.ecloset.user.local.OutfitItemData
import com.example.ecloset.user.local.UserInfo

class LocalDataSource {
	fun getUser(): UserInfo = MockData.testUser
	fun getClothes(): List<ClosetItemData> = MockData.clothesList
	fun getOutfits(): List<OutfitItemData> = MockData.outfitsList
}
