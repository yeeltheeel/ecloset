package com.example.ecloset.user.local

import androidx.annotation.StringRes

const val MIN_PASSWORD_LENGTH = 6
const val CODE_LEN = 4

data class LoginFormState(
	val email: String = "",
	val password: String = "",
	val confirmCode: String? = null,
	@StringRes val emailError: Int? = null,
	@StringRes val passwordError: Int? = null,
	@StringRes val codeError: Int? = null,
	val isSubmitting: Boolean = false,
)

data class RegFormState(
	val username: String = "",
	val email: String = "",
	val password: String = "",
	val code: String = "",
	val confirmPassword: String = "",
	@StringRes val usernameError: Int? = null,
	@StringRes val emailError: Int? = null,
	@StringRes val passwordError: Int? = null,
	@StringRes val confirmPasswordError: Int? = null,
	val isSubmitting: Boolean = false,
)

sealed interface AuthResult {
	data class Success(
		val token: String,
		val userId: String
	) : AuthResult

	data class Failure(
		@StringRes val message: Int
	) : AuthResult
}