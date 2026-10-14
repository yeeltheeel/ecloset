package com.example.ecloset.user.data

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.sql.Date
import com.example.ecloset.user.data.MockData.testUser

data class UserInfo(
	val id: Int,
	val username: String,
	val email: String,
	val avatarUrl: String? = null,
	val birthday: Date? = null,
	// val theme: String = "Violet" // presets or "Dynamic"
)

data class UserState(
	val user: UserInfo? = null,
	val isLoading: Boolean = false,
	val loggedIn: Boolean = false,
	val error: Int? = null
)

class UserViewModel(): ViewModel() {
	private val userStateFlow = MutableStateFlow(UserState())
	val userState: StateFlow<UserState> = userStateFlow.asStateFlow()

	init {
		loadAccount()
	}

	fun loadAccount(){
		viewModelScope.launch {
			userStateFlow.value = userStateFlow.value.copy(
				isLoading = true,
				error = null
			)
		}

		// placeholder for now
		val loggedIn = true
		userStateFlow.value = userStateFlow.value.copy(
			isLoading = false,
			loggedIn = loggedIn,
			user = if (loggedIn) testUser else null
		)
	}

	fun updateAccount(){
		// update profile
	}

	fun logOut(){
		userStateFlow.value = userStateFlow.value.copy(
			loggedIn = false,
			user = null,
			error = null
		)
	}

	fun deleteAccount(){
		// remove account
	}
}
