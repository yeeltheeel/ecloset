package com.example.ecloset.user.local.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecloset.user.local.ConnectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

@HiltViewModel
class ConnectionViewModel @Inject constructor(
	app: Application
): AndroidViewModel(
	application = app
){
	private val _state = MutableStateFlow<ConnectionState>(ConnectionState.Loading)
	val state: StateFlow<ConnectionState> = _state.asStateFlow()
	private val appContext: Context get() = getApplication<Application>().applicationContext

	init{
		check()
	}

	fun check(){
		viewModelScope.launch {
			_state.value = ConnectionState.Loading
			_state.value = if (isOnline()) ConnectionState.Online else ConnectionState.Offline
		}
	}

	suspend fun isOnline(): Boolean {
		return isNetworkAvailable() && canReachBackend()
	}

	fun retry() = check()

	private fun isNetworkAvailable(): Boolean {
		val connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
		val network = connectivityManager.activeNetwork ?: return false
		val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
		val hasInternet = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
				capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
		return hasInternet
	}

	private suspend fun canReachBackend(): Boolean = withContext(Dispatchers.IO) {
		var conn: HttpURLConnection? = null
		try {
			val url = URL("https://www.google.com/generate_204") // path that returns 200 quickly and needs no auth
			conn = (url.openConnection() as HttpURLConnection).apply {
				requestMethod = "HEAD"
				connectTimeout = 3000
				readTimeout = 3000
				useCaches = false
			}
			conn.connect()
			conn.responseCode == 204 // conn.responseCode in 200..399
		} catch (_: Exception) {
			false
		} finally {
			conn?.disconnect()
		}
	}
}
