package com.example.ecloset.user.data

import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.core.content.edit

enum class ConnectionMode {
	AUTO,         // try remote, fallback to mock on failure
	FORCE_REMOTE, // always remote, surface errors (for backend testing)
	FORCE_MOCK    // always mock (offline UI dev)
}

class ConnectionModeManager(private val prefs: SharedPreferences) {
	private val _mode = MutableStateFlow(
		ConnectionMode.valueOf(
			prefs.getString("conn_mode", "AUTO") ?: "AUTO"
		)
	)
	val mode: StateFlow<ConnectionMode> = _mode.asStateFlow()

	fun setMode(newMode: ConnectionMode) {
		_mode.value = newMode
		prefs.edit {
			putString("conn_mode", newMode.name)
		}
	}

	fun current(): ConnectionMode = _mode.value
}
