package com.example.ecloset.user.data

import android.util.Patterns
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecloset.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginFormState(
	val email: String = "",
	val password: String = "",
	val confirmCode: Int? = null, // otp to email
	val emailError: Int? = null,
	val passwordError: Int? = null,
	val codeError: Int? = null,
	val isSubmitting: Boolean = false
)

data class RegFormState(
	val username: String = "",
	val email: String = "",
	val password: String = "",
	val confirmPassword: String = "",
	val usernameError: Int? = null,
	val emailError: Int? = null,
	val passwordError: Int? = null,
	val confirmPasswordError: Int? = null,
	val isSubmitting: Boolean = false
)

sealed interface AuthResult {
	data class Success(val token: String, val userId: String) : AuthResult
	data class Failure(val message: String) : AuthResult
}

const val MINPASS: Int = 6

class AuthViewModel(): ViewModel() {
	// registration
	private val regFormStateFlow = MutableStateFlow(RegFormState())
	val regFormState: StateFlow<RegFormState> = regFormStateFlow.asStateFlow()
	// login
	private val loginFormStateFlow = MutableStateFlow(LoginFormState())
	val loginFormState: StateFlow<LoginFormState> = loginFormStateFlow.asStateFlow()
	// general
	private val authResultFlow = MutableStateFlow<AuthResult?>(null)
	val authResult: StateFlow<AuthResult?> = authResultFlow.asStateFlow()

	// field handling
	fun updateRegField(field: String, value: String) {
		val current = regFormStateFlow.value
		regFormStateFlow.value = when (field) {
			"username" -> current.copy(
				username = value,
				usernameError = null
			)
			"email" -> current.copy(
				email = value,
				emailError = null
			)
			"password" -> current.copy(
				password = value,
				passwordError = null
			)
			"confirmPassword" -> current.copy(
				confirmPassword = value,
				confirmPasswordError = null
			)
			else -> current
		}
	}

	fun updateLoginField(field: String, value: String) {
		val current = loginFormStateFlow.value
		loginFormStateFlow.value = when (field) {
			"email" -> current.copy(
				email = value,
				emailError = null
			)
			"password" -> current.copy(
				password = value,
				passwordError = null
			)
			"confirmCode" -> {
				val code = value.toIntOrNull()
				current.copy(
					confirmCode = code,
					codeError = null
				)
			}
			else -> current
		}
	}

	// actions
	fun register(){
		viewModelScope.launch{
			val current = regFormStateFlow.value
			val errors = mutableMapOf<String, Int>()

			if (current.username.isBlank())
				errors["username"] = R.string.username_error
			if (current.email.isBlank() ||
				!Patterns.EMAIL_ADDRESS.matcher(current.email).matches()) {
				errors["email"] = R.string.email_error
			}
			if (current.password.length < MINPASS)
				errors["password"] = R.string.password_error
			if (current.password != current.confirmPassword)
				errors["confirmPassword"] = R.string.confirm_password_error

			if (errors.isNotEmpty()) {
				regFormStateFlow.value = current.copy(
					usernameError = errors["username"],
					emailError = errors["email"],
					passwordError = errors["password"],
					confirmPasswordError = errors["confirmPassword"]
				)
				return@launch
			}

			regFormStateFlow.value = current.copy(isSubmitting = true)
			authResultFlow.value = null

			// save data here

			//authResultFlow.value = result
			regFormStateFlow.value = regFormStateFlow.value.copy(isSubmitting = false)
		}
	}

	fun login(){
		viewModelScope.launch {
			val current = loginFormStateFlow.value
			val errors = mutableMapOf<String, Int>()

			if (current.email.isBlank() ||
				!Patterns.EMAIL_ADDRESS.matcher(current.email)
					.matches()
			) {
				errors["email"] = R.string.email_error
			}
			if (current.password.length < MINPASS)
				errors["password"] = R.string.password_error

			if (errors.isNotEmpty()) {
				loginFormStateFlow.value = current.copy(
					emailError = errors["email"],
					passwordError = errors["password"]
				)
				return@launch
			}

			loginFormStateFlow.value = current.copy(isSubmitting = true)
			authResultFlow.value = null

			// save data here

			//authResultFlow.value = result
			loginFormStateFlow.value = loginFormStateFlow.value.copy(isSubmitting = false)
		}
	}

	fun clearResult() {
		authResultFlow.value = null
	}
}