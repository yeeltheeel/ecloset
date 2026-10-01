package com.example.ecloset.user.data

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.sql.Date

data class UserInfo(
	val id: Int,
	val username: String,
	val email: String,
	val avatarUrl: String? = null
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

		// placeholder
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
	}
}

val testUser = UserInfo(
	id = 1,
	username = "User",
	email = "user@example.com",
	avatarUrl = null
)

// user related objects
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
