package com.example.ecloset.user.remote.api

import com.example.ecloset.user.remote.dto.OutfitItemDto
import retrofit2.http.GET
import retrofit2.http.Path

interface OutfitsApi {
	@GET("api/users/{userId}/outfits")
	suspend fun getOutfits(@Path("userId") userId: Int): List<OutfitItemDto>
}
