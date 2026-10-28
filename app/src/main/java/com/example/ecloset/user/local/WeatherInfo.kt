package com.example.ecloset.user.local

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecloset.user.remote.api.WttrApiClient
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

data class WttrResponse(
	@SerializedName("current_condition")
	val currentCondition: List<CurrentCondition> = emptyList(),
	@SerializedName("nearest_area")
	val nearestArea: List<NearestArea> = emptyList()
)

data class CurrentCondition(
	@SerializedName("temp_C")
	val tempC: String = "0",
	@SerializedName("humidity")
	val humidity: String = "0",
	@SerializedName("windspeedKmph")
	val windSpeedKmph: String = "0",
	@SerializedName("weatherDesc")
	val weatherDesc: List<WeatherDesc> = emptyList()
)

data class WeatherDesc(
	@SerializedName("value")
	val value: String = ""
)

data class NearestArea(
	@SerializedName("areaName")
	val areaName: List<AreaName> = emptyList()
)

data class AreaName(
	@SerializedName("value")
	val value: String = ""
)

data class CurrentWeather(
	val temperature: Double,
	val humidity: Int,
	val windSpeed: Double,
	val weatherCode: Int,
	val description: String
)

sealed interface WeatherUiState {
	data object Loading : WeatherUiState
	data class Success(
		val weather: CurrentWeather
	) : WeatherUiState
	data class Error(
		val message: String
	) : WeatherUiState
}

class WeatherViewModel : ViewModel() {
	private val _state = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
	val state: StateFlow<WeatherUiState> = _state.asStateFlow()
	private var lastLoadedAt: Long = 0L
	private var fetchJob: Job? = null
	private var periodicJob: Job? = null

	companion object {
		private const val REFRESH_INTERVAL_MS = 3 * 60 * 60 * 1000L  // 3 hours
		private const val STALE_AFTER_MS      = 30 * 60 * 1000L      // 30 minutes
	}

	fun loadWeather(latitude: Double, longitude: Double) {
		fetchJob?.cancel()
		fetchJob = viewModelScope.launch {
			_state.value = WeatherUiState.Loading
			_state.value = try {
				val location = "$latitude,$longitude"
				val response = WttrApiClient.api.getCurrentWeather(location)
				val cc = response.currentCondition.firstOrNull()
					?: return@launch run {
						_state.value = WeatherUiState.Error("Empty response from wttr.in")
					}

				val mapped = CurrentWeather(
					temperature = cc.tempC.toDoubleOrNull() ?: 0.0,
					humidity = cc.humidity.toIntOrNull() ?: 0,
					windSpeed = cc.windSpeedKmph.toDoubleOrNull() ?: 0.0,
					weatherCode = -1, // wttr.in doesn't use WMO codes
					description = cc.weatherDesc.firstOrNull()?.value ?: "-"
				)

				lastLoadedAt = System.currentTimeMillis()

				WeatherUiState.Success(mapped)
			} catch (e: Exception) {
				WeatherUiState.Error(e.message ?: "Failed to load weather")
			}
		}
	}

	fun loadWeatherIfNeeded(latitude: Double, longitude: Double) {
		val isStale = System.currentTimeMillis() - lastLoadedAt > STALE_AFTER_MS
		if (isStale || _state.value is WeatherUiState.Error) {
			loadWeather(latitude, longitude)
		}
		startPeriodicRefresh(latitude, longitude)
	}

	fun refresh(latitude: Double, longitude: Double) {
		loadWeather(latitude, longitude)
	}

	private fun startPeriodicRefresh(latitude: Double, longitude: Double) {
		if (periodicJob?.isActive == true) return

		periodicJob = viewModelScope.launch {
			while (isActive) {
				delay(REFRESH_INTERVAL_MS.milliseconds)
				loadWeather(latitude, longitude)
			}
		}
	}

	override fun onCleared() {
		super.onCleared()
		fetchJob?.cancel()
		periodicJob?.cancel()
	}
}
