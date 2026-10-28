package com.example.ecloset.user.remote.api

import com.example.ecloset.user.remote.dto.UserDto
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {
	@GET("api/users/{userId}")
	suspend fun getUser(@Path("userId") userId: Int): UserDto
}
