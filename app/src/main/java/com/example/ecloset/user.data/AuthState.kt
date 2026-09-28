package com.example.ecloset.user.data

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