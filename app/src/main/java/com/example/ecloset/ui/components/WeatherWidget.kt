package com.example.ecloset.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ecloset.user.local.CurrentWeather
import com.example.ecloset.user.local.WeatherUiState
import com.example.ecloset.user.local.WeatherViewModel
import kotlin.math.roundToInt

@Composable
fun WeatherWidget(
	modifier: Modifier = Modifier,
	location: String,
	latitude: Double,
	longitude: Double,
	viewModel: WeatherViewModel = viewModel()
) {
	val state by viewModel.state.collectAsState()
	val onRefresh: () -> Unit = {
		viewModel.loadWeather(latitude, longitude)
	}

	LaunchedEffect(latitude, longitude) {
		viewModel.loadWeather(latitude, longitude)
	}

	Card(
		modifier = modifier
			.height(150.dp)
			.fillMaxWidth()
			.padding(horizontal = 16.dp),
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.surface
		),
		elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
	) {
		when (val s = state) {
			is WeatherUiState.Loading -> LoadingContent()
			is WeatherUiState.Error -> ErrorContent(s.message, onRefresh)
			is WeatherUiState.Success -> SuccessContent(location, s.weather, onRefresh)
		}
	}
	Spacer(modifier = Modifier.padding(vertical = 8.dp))
}

@Composable
private fun LoadingContent() {
	Box(
		modifier = Modifier.fillMaxSize(),
		contentAlignment = Alignment.Center
	) {
		CircularProgressIndicator()
	}
}

@Composable
private fun ErrorContent(
	message: String,
	onRefresh: () -> Unit
) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Text(
			text = message,
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.error,
			textAlign = TextAlign.Center
		)
		Spacer(modifier = Modifier.height(8.dp))
		IconButton(onClick = onRefresh) {
			Icon(
				imageVector = Icons.Default.Refresh,
				contentDescription = "Retry",
				tint = MaterialTheme.colorScheme.primary
			)
		}
	}
}

@Composable
private fun SuccessContent(
	location: String,
	weather: CurrentWeather,
	onRefresh: () -> Unit
) {
	LazyRow(
		modifier = Modifier
			.fillMaxHeight()
			.padding(16.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		item {
			Icon(
				imageVector = weatherIcon(weather),
				contentDescription = weather.description,
				modifier = Modifier
					.fillMaxHeight()
					.aspectRatio(1f),
				tint = MaterialTheme.colorScheme.primary
			)
		}
		item {
			Column(
				modifier = Modifier
					.fillMaxHeight()
					.padding(start = 16.dp),
				verticalArrangement = Arrangement.Center
			) {
				Text(
					text = location,
					style = MaterialTheme.typography.titleMedium,
					color = MaterialTheme.colorScheme.primary
				)
				Text(
					text = "${weather.temperature.roundToInt()}°C",
					style = MaterialTheme.typography.titleLarge,
					modifier = Modifier.padding(vertical = 4.dp),
					color = MaterialTheme.colorScheme.primary
				)
				Text(
					text = weather.description,
					style = MaterialTheme.typography.bodySmall,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
			}
		}
		item {
			Column(
				modifier = Modifier
					.fillMaxHeight()
					.padding(start = 24.dp),
				verticalArrangement = Arrangement.Center
			) {
				Text(
					text = "💧 ${weather.humidity}%",
					style = MaterialTheme.typography.bodyMedium
				)
				Text(
					text = "💨 ${weather.windSpeed.roundToInt()} km/h",
					style = MaterialTheme.typography.bodyMedium,
					modifier = Modifier.padding(top = 4.dp)
				)
			}
		}
		item {
			Column(
				modifier = Modifier
					.fillMaxHeight()
					.padding(start = 12.dp),
				verticalArrangement = Arrangement.Center,
				horizontalAlignment = Alignment.End
			) {
				IconButton(onClick = onRefresh) {
					Icon(
						imageVector = Icons.Default.Refresh,
						contentDescription = "Refresh weather",
						tint = MaterialTheme.colorScheme.primary
					)
				}
			}
		}
	}
}

// icons

private fun weatherIcon(weather: CurrentWeather): ImageVector {
	val d = weather.description.lowercase()
	return when {
		"sun" in d || "clear" in d -> Icons.Default.WbSunny
		"rain" in d || "drizzle" in d || "shower" in d -> Icons.Default.WaterDrop
		"snow" in d || "sleet" in d || "blizzard" in d -> Icons.Default.WaterDrop
		"cloud" in d || "overcast" in d || "fog" in d || "mist" in d -> Icons.Default.Cloud
		"thunder" in d || "storm" in d -> Icons.Default.Air
		else -> Icons.Default.Air
	}
}

@Suppress("unused")
private fun weatherIcon(code: Int): ImageVector = when (code) {
	0 -> Icons.Default.WbSunny
	1, 2 -> Icons.Default.Cloud
	3 -> Icons.Default.Cloud
	45, 48 -> Icons.Default.Cloud
	51, 53, 55, 56, 57 -> Icons.Default.WaterDrop
	61, 63, 65, 66, 67, 80, 81, 82 -> Icons.Default.WaterDrop
	71, 73, 75, 77, 85, 86 -> Icons.Default.WaterDrop
	95, 96, 99 -> Icons.Default.Air
	else -> Icons.Default.Air
}

@Suppress("unused")
private fun weatherDescription(code: Int): String = when (code) {
	0 -> "Clear sky"
	1 -> "Mainly clear"
	2 -> "Partly cloudy"
	3 -> "Overcast"
	45, 48 -> "Fog"
	51, 53, 55 -> "Drizzle"
	56, 57 -> "Freezing drizzle"
	61, 63, 65 -> "Rain"
	66, 67 -> "Freezing rain"
	71, 73, 75 -> "Snowfall"
	77 -> "Snow grains"
	80, 81, 82 -> "Rain showers"
	85, 86 -> "Snow showers"
	95 -> "Thunderstorm"
	96, 99 -> "Thunderstorm with hail"
	else -> "Unknown"
}