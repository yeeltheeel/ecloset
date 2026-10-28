package com.example.ecloset.user.local.datasource

import com.example.ecloset.user.data.MockData
import com.example.ecloset.user.remote.dto.AuthResponseDto

class AuthLocalDataSource {
	fun register(): AuthResponseDto = AuthResponseDto(
		token = "mock-token-register",
		userId = MockData.testUser.id.toString()
	)

	fun login(): AuthResponseDto = AuthResponseDto(
		token = "mock-token-login",
		userId = MockData.testUser.id.toString()
	)
}