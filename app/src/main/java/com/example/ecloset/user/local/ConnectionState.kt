package com.example.ecloset.user.local

sealed interface ConnectionState {
	data object Loading : ConnectionState
	data object Online : ConnectionState
	data object Offline : ConnectionState
}
