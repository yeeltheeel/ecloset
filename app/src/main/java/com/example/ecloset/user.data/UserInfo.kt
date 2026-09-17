package com.example.ecloset.user.data

data class UserInfo(
	val username: String,
	val avatarUrl: String?
)

val user = UserInfo(
	username = "User",
	avatarUrl = null
)