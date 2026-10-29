package com.example.ecloset.user.data

import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.core.content.edit
import com.example.ecloset.BuildConfig

data class Session(
	val userId: Int,
	val token: String
)

enum class DevLoginOverride {
	NONE,
	FORCE_IN,
	FORCE_OUT
}

class SessionManager(private val prefs: SharedPreferences) {
	private val _session = MutableStateFlow<Session?>(null)
	val session: StateFlow<Session?> = _session.asStateFlow()

	companion object {
		private const val KEY_USER_ID = "session_user_id"
		private const val KEY_TOKEN   = "session_token"
		private const val KEY_DEV_OVERRIDE = "session_dev_override"
	}

	fun setSession(userId: Int, token: String) {
		val s = Session(userId, token)
		_session.value = s
		prefs.edit {
			putInt(KEY_USER_ID, s.userId)
			putString(KEY_TOKEN, s.token)
		}
	}

	fun currentUserId(): Int? = _session.value?.userId
	fun currentToken(): String? = _session.value?.token


	private fun loadFromPrefs(): Session? {
		val id = prefs.getInt(KEY_USER_ID, -1)
		val token = prefs.getString(KEY_TOKEN, null)

		if (id != -1 && !token.isNullOrEmpty()) {
			return Session(id, token)
		}

		// auto-login as mock user 1 when nothing is saved
		if (BuildConfig.DEBUG) {
			val devSession = Session(userId = 1, token = "dev-token")
			prefs.edit {
				putInt(KEY_USER_ID, devSession.userId)
				putString(KEY_TOKEN, devSession.token)
			}
			return devSession
		}

		return null
	}

	fun clear() {
		_session.value = null
		prefs.edit {
			remove(KEY_USER_ID)
			remove(KEY_TOKEN)
		}
	}

	fun devOverride(): DevLoginOverride {
		if (!BuildConfig.DEBUG) return DevLoginOverride.NONE
		return when (prefs.getString(KEY_DEV_OVERRIDE, null)) {
			"IN"  -> DevLoginOverride.FORCE_IN
			"OUT" -> DevLoginOverride.FORCE_OUT
			else  -> DevLoginOverride.NONE
		}
	}

	fun setDevOverride(override: DevLoginOverride) {
		if (!BuildConfig.DEBUG) return
		prefs.edit {
			when (override) {
				DevLoginOverride.NONE      -> remove(KEY_DEV_OVERRIDE)
				DevLoginOverride.FORCE_IN  -> putString(KEY_DEV_OVERRIDE, "IN")
				DevLoginOverride.FORCE_OUT -> putString(KEY_DEV_OVERRIDE, "OUT")
			}
		}
		// Re-evaluate the current state
		_session.value = loadFromPrefs()
	}
}
