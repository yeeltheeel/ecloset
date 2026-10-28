package com.example.ecloset.user.remote.api

import com.example.ecloset.user.remote.dto.AuthResponseDto
import com.example.ecloset.user.remote.dto.LoginRequestDto
import com.example.ecloset.user.remote.dto.RegisterRequestDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
	@POST("api/auth/register")
	suspend fun register(
		@Body body: RegisterRequestDto
	): AuthResponseDto

	@POST("api/auth/login")
	suspend fun login(
		@Body body: LoginRequestDto
	): AuthResponseDto
}