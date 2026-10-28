package com.example.ecloset.user.local

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import com.example.ecloset.user.local.MockData.testUser
import kotlinx.coroutines.flow.update

data class UserInfo(
	val id: Int,
	val username: String,
	val email: String,
	val avatarUrl: String? = null,
	val birthday: LocalDate? = null,
	val locationDynamic: Boolean = true,
	//val location: String = "Moscow",
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

// localize later (prob will be list<int>)
val selectOptions = listOf("Static", "Dynamic")
val locations = emptyList<LocationInfo>()
val presets = listOf("None", "Violet", "Sage", "Amber", "Indigo", "Rose")

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

class UserViewModel: ViewModel() {
	private val _userState = MutableStateFlow(UserState())
	val userState: StateFlow<UserState> = _userState.asStateFlow()

	init {
		loadAccount()
	}

	fun loadAccount(){
		viewModelScope.launch {
			_userState.update {
				it.copy(
					isLoading = true,
					error = null
				)
			}

			// placeholder for now
			val loggedIn = true
			val user = if (loggedIn) testUser else null

			_userState.update {
				it.copy(
					isLoading = false,
					loggedIn = loggedIn,
					user = user
				)
			}
		}
	}

	fun updateAccount(updated: UserInfo){
		_userState.update {
			it.copy(user = updated)
		}
	}

	private inline fun updateUser(
		transform: (UserInfo) -> UserInfo
	) {
		_userState.update { state ->
			val current = state.user ?: return@update state
			state.copy(user = transform(current))
		}
	}

	fun updateUsername(username: String) {
		updateUser {
			it.copy(username = username)
		}
	}

	fun updateEmail(email: String) {
		updateUser {
			it.copy(email = email)
		}
	}

	fun updateAvatarUrl(url: String?) {
		updateUser {
			it.copy(avatarUrl = url)
		}
	}

	fun updateBirthday(birthday: LocalDate?) {
		updateUser {
			it.copy(birthday = birthday)
		}
	}

	fun updateLocationDynamic(enabled: Boolean) {
		updateUser {
			it.copy(locationDynamic = enabled)
		}
	}

	fun updateThemeDynamic(enabled: Boolean) {
		updateUser {
			it.copy(themeDynamic = enabled)
		}
	}

	fun updateThemePreset(preset: String) {
		updateUser {
			it.copy(themePreset = preset)
		}
	}

	fun logOut(){
		_userState.update {
			it.copy(
				loggedIn = false,
				user = null,
				error = null
			)
		}
	}

	fun deleteAccount(){
		// call remote
		logOut()
	}

	fun updateLocation(location: LocationInfo) {
		updateUser {
			it.copy(location = location)
		}
	}
}
