package com.example.ecloset.user.local.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecloset.R
import com.example.ecloset.user.data.MockData.testUser
import com.example.ecloset.user.data.repo.UserRepository
import com.example.ecloset.user.local.LocationInfo
import com.example.ecloset.user.local.UpdateResult
import com.example.ecloset.user.local.UserInfo
import com.example.ecloset.user.local.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
	private val userRepo: UserRepository
): ViewModel() {
	private val _userState = MutableStateFlow(UserState())
	val userState: StateFlow<UserState> = _userState.asStateFlow()
	private val _updateResult = MutableStateFlow<UpdateResult?>(null)
	val updateResult: StateFlow<UpdateResult?> = _updateResult.asStateFlow()
	val currentUserId = 1
	var user = UserInfo(-1, "", "")
	var loggedIn = false

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

			try {
				user = userRepo.getUser(currentUserId)
				_userState.update {
					it.copy(user = user, isLoading = false)
				}
				loggedIn = true
			} catch (e: Exception) {
				_userState.update {
					it.copy(isLoading = false)
				}
				_updateResult.value = UpdateResult.Invalid(
					R.string.error_generic
				)
			}

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
		// weatherViewModel.refresh(location.latitude, location.longitude)
	}
}