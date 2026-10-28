package com.example.ecloset.user.remote.datasource

import com.example.ecloset.user.remote.api.AuthApi
import com.example.ecloset.user.remote.dto.AuthResponseDto
import com.example.ecloset.user.remote.dto.LoginRequestDto
import com.example.ecloset.user.remote.dto.RegisterRequestDto

class AuthRemoteDataSource(private val api: AuthApi) {
	suspend fun register(
		username: String, email: String, password: String, code: String
	): Result<AuthResponseDto> = runCatching {
		api.register(RegisterRequestDto(username, email, password, code))
	}

	suspend fun login(
		email: String, password: String, code: String
	): Result<AuthResponseDto> = runCatching {
		api.login(LoginRequestDto(email, password, code))
	}
}
