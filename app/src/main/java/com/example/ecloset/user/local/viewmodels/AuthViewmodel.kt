package com.example.ecloset.user.local.viewmodels

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecloset.R
import com.example.ecloset.user.data.repo.AuthRepository
import com.example.ecloset.user.local.AuthResult
import com.example.ecloset.user.local.LoginFormState
import com.example.ecloset.user.local.MIN_PASSWORD_LENGTH
import com.example.ecloset.user.local.RegFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
	private val authRepository: AuthRepository
): ViewModel() {

	private val _regFormState = MutableStateFlow(RegFormState())
	val regFormState: StateFlow<RegFormState> = _regFormState.asStateFlow()

	private val _loginFormState = MutableStateFlow(LoginFormState())
	val loginFormState: StateFlow<LoginFormState> = _loginFormState.asStateFlow()

	private val _authResult = MutableStateFlow<AuthResult?>(null)
	val authResult: StateFlow<AuthResult?> = _authResult.asStateFlow()

	fun updateRegUsername(value: String) = _regFormState.update {
		it.copy(
			username = value,
			usernameError = null
		)
	}

	fun updateRegEmail(value: String) = _regFormState.update {
		it.copy(
			email = value,
			emailError = null
		)
	}

	fun updateRegPassword(value: String) = _regFormState.update {
		it.copy(
			password = value,
			passwordError = null
		)
	}

	fun updateRegConfirmPassword(value: String) = _regFormState.update {
		it.copy(
			confirmPassword = value,
			confirmPasswordError = null
		)
	}

	fun updateRegCode(value: String) = _regFormState.update {
		// email code, check if value is same as sent
		it.copy(
			code = value
		)
	}

	fun updateLoginEmail(value: String) = _loginFormState.update {
		it.copy(
			email = value,
			emailError = null
		)
	}

	fun updateLoginPassword(value: String) = _loginFormState.update {
		it.copy(
			password = value,
			passwordError = null
		)
	}

	fun updateLoginCode(value: String) = _loginFormState.update {
		it.copy(
			confirmCode = value,
			codeError = null
		)
	}

	fun register() {
		val current = _regFormState.value

		val usernameError = if (current.username.isBlank())
			R.string.username_error else null
		val emailError = if (!current.email.isValidEmail())
			R.string.email_error else null
		val passwordError = if (current.password.length < MIN_PASSWORD_LENGTH)
			R.string.password_error else null
		val confirmError = if (current.password != current.confirmPassword)
			R.string.confirm_password_error else null

		val hasErrors = listOfNotNull(
			usernameError,
			emailError,
			passwordError,
			confirmError
		).isNotEmpty()

		if (hasErrors) {
			_regFormState.update {
				it.copy(
					usernameError = usernameError,
					emailError = emailError,
					passwordError = passwordError,
					confirmPasswordError = confirmError
				)
			}
			return
		}

		viewModelScope.launch {
			_regFormState.update { it.copy(isSubmitting = true) }
			_authResult.value = null

			val result = try {
				val response = authRepository.register(
					username = current.username,
					email = current.email,
					password = current.password,
					code = current.code
				)
				AuthResult.Success(token = response.token, userId = response.userId)
			} catch (e: Exception) {
				AuthResult.Failure(R.string.error_generic)
			}

			_authResult.value = result
			_regFormState.update { it.copy(isSubmitting = false) }
		}
	}

	fun login() {
		val current = _loginFormState.value

		val emailError = if (!current.email.isValidEmail())
			R.string.email_error else null
		val passwordError = if (current.password.length < MIN_PASSWORD_LENGTH)
			R.string.password_error else null

		if (emailError != null || passwordError != null) {
			_loginFormState.update {
				it.copy(
					emailError = emailError,
					passwordError = passwordError
				)
			}
			return
		}

		viewModelScope.launch {
			_loginFormState.update { it.copy(isSubmitting = true) }
			_authResult.value = null

			val result = try {
				val response = authRepository.login(
					email = current.email,
					password = current.password,
					code = current.confirmCode
				)
				AuthResult.Success(token = response.token, userId = response.userId)
			} catch (e: Exception) {
				AuthResult.Failure(R.string.error_generic)
			}

			_authResult.value = result
			_loginFormState.update { it.copy(isSubmitting = false) }
		}
	}

	fun clearResult() {
		_authResult.value = null
	}
}

private fun String.isValidEmail(): Boolean =
	isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
