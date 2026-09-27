package com.example.ecloset.user.data

import androidx.compose.ui.graphics.Color
import java.sql.Date

data class UserInfo(
	val id: Int,
	val username: String,
	val avatarUrl: String?
)

data class LoginFormState(
	val email: String = "",
	val password: String = "",
	val emailError: String? = null,
	val passwordError: String? = null,
	val isSubmitting: Boolean = false
)

data class RegFormState(
	val username: String = "",
	val email: String = "",
	val password: String = "",
	val confirmPassword: String = "",
	val usernameError: String? = null,
	val emailError: String? = null,
	val passwordError: String? = null,
	val confirmPasswordError: String? = null,
	val isSubmitting: Boolean = false
)

sealed interface AuthResult {
	data class Success(val token: String, val userId: String) : AuthResult
	data class Failure(val message: String) : AuthResult
}

interface GridCardData {
	val id: Int
	val imageUrl: String?
	val dateAdded: Date
}

data class ClosetItem(
	override val id: Int,
	override val imageUrl: String?,
	val category: String,
	val season: String,
	val material: String,
	val color: Color,
	val occasion: String,
	override val dateAdded: Date
) : GridCardData

data class OutfitItem(
	override val id: Int,
	override val imageUrl: String?,
	val season: String, // or Int if we go with season_id
	val occasion: String,
	override val dateAdded: Date
) : GridCardData

val testUser = UserInfo(
	id = 1,
	username = "User",
	avatarUrl = null
)