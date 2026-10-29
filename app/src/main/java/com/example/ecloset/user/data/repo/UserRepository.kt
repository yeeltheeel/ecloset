package com.example.ecloset.user.data.repo

import com.example.ecloset.user.data.ConnectionMode
import com.example.ecloset.user.data.ConnectionModeManager
import com.example.ecloset.user.local.ClosetItemData
import com.example.ecloset.user.local.UserInfo
import com.example.ecloset.user.local.datasource.LocalDataSource
import com.example.ecloset.user.remote.datasource.UserRemoteDataSource

class UserRepository(
	private val remote: UserRemoteDataSource,
	private val local: LocalDataSource,
	private val modeManager: ConnectionModeManager
) {
	private var currentUser: UserInfo = local.getUser()

	suspend fun getUser(userId: Int): UserInfo {
		val result = when (modeManager.current()) {
			ConnectionMode.FORCE_MOCK -> local.getUser()
			ConnectionMode.FORCE_REMOTE -> remote.getUser(userId).getOrThrow()
			ConnectionMode.AUTO -> remote.getUser(userId).getOrNull() ?: local.getUser()
		}
		return result
	}

	fun deleteUser(userId: Int){
		// todo: remote
	}
}
