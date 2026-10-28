package com.example.ecloset.user.local

import androidx.annotation.StringRes
import java.time.LocalDate

data class UserInfo(
	val id: Int,
	val username: String,
	val email: String,
	val avatarUrl: String? = null,
	val birthday: LocalDate? = null,
	val locationDynamic: Boolean = true,
	val location: LocationInfo = LocationInfo(
		location = "Moscow",
		latitude = 55.7558,
		longitude = 37.6173
	),
	val themeDynamic: Boolean = false,
	val themePreset: String = "Violet"
)

data class LocationInfo(
	val location: String,
	val latitude: Double,
	val longitude: Double
)

data class UserState(
	val user: UserInfo? = null,
	val isLoading: Boolean = false,
	val loggedIn: Boolean = false,
	val error: Int? = null
)

sealed interface UpdateResult {
	data object Success : UpdateResult
	data class Invalid(
		@StringRes val message: Int
	) : UpdateResult
}
