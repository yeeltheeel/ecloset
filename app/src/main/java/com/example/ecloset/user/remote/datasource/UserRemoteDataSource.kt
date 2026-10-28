package com.example.ecloset.user.remote.datasource

import com.example.ecloset.user.local.UserInfo
import com.example.ecloset.user.remote.api.UserApi
import com.example.ecloset.user.remote.dto.toUi

class UserRemoteDataSource(
	private val api: UserApi
) {
	suspend fun getUser(
		userId: Int
	): Result<UserInfo> = runCatching {
		api.getUser(userId).toUi()
	}
}
