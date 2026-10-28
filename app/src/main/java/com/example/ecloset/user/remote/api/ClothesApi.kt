package com.example.ecloset.user.remote.api

import com.example.ecloset.user.remote.dto.ClosetItemDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ClothesApi {
	@GET("api/users/{userId}/clothes")
	suspend fun getClothes(@Path("userId") userId: Int): List<ClosetItemDto>
}
