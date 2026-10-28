package com.example.ecloset.user.remote.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
	@SerializedName("id")
	val id: Int,
	@SerializedName("username")
	val username: String,
	@SerializedName("email")
	val email: String,
	@SerializedName("avatarUrl")
	val avatarUrl: String?
)