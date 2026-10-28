package com.example.ecloset.user.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisterRequestDto(
	@SerializedName("username")
	val username: String,
	@SerializedName("email")
	val email: String,
	@SerializedName("password")
	val password: String,
	@SerializedName("code")
	val code: String
)

data class LoginRequestDto(
	@SerializedName("email")
	val email: String,
	@SerializedName("password")
	val password: String,
	@SerializedName("code")
	val code: String
)

data class AuthResponseDto(
	@SerializedName("token")
	val token: String,
	@SerializedName("userId")
	val userId: String
)