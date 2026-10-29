package com.example.ecloset.user.data.repo

import com.example.ecloset.user.data.ConnectionMode
import com.example.ecloset.user.data.ConnectionModeManager
import com.example.ecloset.user.local.datasource.AuthLocalDataSource
import com.example.ecloset.user.remote.datasource.AuthRemoteDataSource
import com.example.ecloset.user.remote.dto.AuthResponseDto

class AuthRepository(
	private val remote: AuthRemoteDataSource,
	private val local: AuthLocalDataSource,
	private val modeManager: ConnectionModeManager
) {
	suspend fun register(
		username: String,
		email: String,
		password: String,
		code: String? = null
	): AuthResponseDto = when (modeManager.current()) {
		ConnectionMode.FORCE_MOCK   -> local.register()
		ConnectionMode.FORCE_REMOTE -> remote.register(
			username,
			email,
			password,
			code ?: ""
		).getOrThrow()
		ConnectionMode.AUTO -> remote.register(
			username,
			email,
			password,
			code ?: ""
		).getOrNull() ?: local.register()
	}

	suspend fun login(
		email: String,
		password: String,
		code: String? = null
	): AuthResponseDto = when (modeManager.current()) {
		ConnectionMode.FORCE_MOCK   -> local.login()
		ConnectionMode.FORCE_REMOTE -> remote.login(
			email,
			password,
			code ?: ""
		).getOrThrow()
		ConnectionMode.AUTO -> remote.login(
			email,
			password,
			code ?: ""
		).getOrNull() ?: local.login()
	}
}
