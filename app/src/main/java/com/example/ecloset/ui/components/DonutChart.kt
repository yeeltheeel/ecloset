package com.example.ecloset.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ecloset.R
import com.example.ecloset.user.local.CategoryItem
import com.example.ecloset.user.local.PreviewCardData
import kotlin.math.atan2
import kotlin.math.hypot
import kotlin.math.min

data class DonutChartData(
	val category: CategoryItem,
	val units: Int,
	val percent: Float,
	val color: Color
)

@Composable
fun DonutChart(
	categories: List<CategoryItem>,
	items: List<PreviewCardData> // will become ClosetItemData
) {
	val colors = listOf(
		Color(0xFF7FB88A),
		Color(0xFF6BA8C9),
		Color(0xFF5B7FB0),
		Color(0xFF6E5FA8),
		Color(0xFFA97BC4),
		Color(0xFFC97C9C),
		Color(0xFFD97C9A),
		Color(0xFFC97C7C),
		Color(0xFFE09368),
		Color(0xFFE0B15A),
		Color(0xFFC9A87C),
		Color(0xFF8FAF9C),
	)

	val numbers = listOf<Int>(10, 5, 7, 2, 3, 15) // until proper data, then calculate

	val data = remember(categories, numbers) {
		val total = numbers.sum().coerceAtLeast(1)
		categories.mapIndexed { index, category ->
			val num = numbers.getOrElse(index) { 0 }
			DonutChartData(
				category = category,
				units = num,
				color = colors[index % colors.size],
				percent = if (total > 0) num.toFloat() / total else 0f
			)
		}
	}

	var selectedIndex by remember { mutableStateOf<Int?>(null) }
	val totalUnits = remember(data) { data.sumOf { it.units } }

	val strokeWidth = 50.dp
	val trackColor = MaterialTheme.colorScheme.surface
	val maxSize = 250.dp

	Box(
		modifier = Modifier
			.fillMaxWidth()
			.padding(
				horizontal = 50.dp,
				vertical = 8.dp
			),
		contentAlignment = Alignment.Center
	) {
		Canvas(
			modifier = Modifier
				.sizeIn(
					maxWidth = maxSize,
					maxHeight = maxSize
				)
				.aspectRatio(1f)
				.pointerInput(data) {
					detectTapGestures { tapOffset ->
						val minDimension = min(size.width, size.height)
						val strokePx = strokeWidth.toPx()
						val diameter = minDimension - strokePx
						val radius = diameter / 2f
						val center = Offset(size.width / 2f, size.height / 2f)

						val dx = tapOffset.x - center.x
						val dy = tapOffset.y - center.y
						val dist = hypot(dx, dy)

						// ignore taps outside the ring band
						val inner = radius - strokePx / 2f
						val outer = radius + strokePx / 2f
						if (dist < inner || dist > outer) {
							selectedIndex = null
							return@detectTapGestures
						}

						// angle in degrees, 0 = 12 o'clock, clockwise
						val angleDeg = ((Math.toDegrees(
							atan2(dy.toDouble(), dx.toDouble())
						) + 90.0 + 360.0) % 360.0).toFloat()

						var accumulated = 0f
						val hit = data.indexOfFirst { item ->
							val sweep = item.percent * 360f
							val inSegment = angleDeg >= accumulated &&
									angleDeg < accumulated + sweep
							accumulated += sweep
							inSegment
						}

						selectedIndex = when {
							hit < 0 -> null
							selectedIndex == hit -> null // toggle off
							else -> hit
						}
					}
				}
		) {
			val strokePx = strokeWidth.toPx()
			val diameter = size.minDimension - strokePx
			val arcSize = Size(diameter, diameter)
			val topLeft = Offset(
				(size.width - diameter) / 2f,
				(size.height - diameter) / 2f,
			)

			// background track
			drawArc(
				color = trackColor,
				startAngle = 0f,
				sweepAngle = 360f,
				useCenter = false,
				topLeft = topLeft,
				size = arcSize,
				style = Stroke(
					width = strokePx,
					cap = StrokeCap.Butt
				),
			)

			// segments
			var startAngle = -90f
			data.forEach { item ->
				val sweep = item.percent * 360f
				if (sweep > 0f) {
					drawArc(
						color = item.color,
						startAngle = startAngle,
						sweepAngle = sweep,
						useCenter = false,
						topLeft = topLeft,
						size = arcSize,
						style = Stroke(
							width = strokePx,
							cap = StrokeCap.Butt
						),
					)
				}
				startAngle += sweep
			}
		}

		// center text overlay
		val selected = selectedIndex?.let { data.getOrNull(it) }
		val displayValue = selected?.units ?: totalUnits
		val displayColor = selected?.color
			?: MaterialTheme.colorScheme.onSurface

		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			modifier = Modifier.padding(horizontal = 24.dp)
		) {
			Text(
				text = displayValue.toString(),
				style = MaterialTheme.typography.headlineMedium,
				color = displayColor,
				textAlign = TextAlign.Center
			)
			Text(
				text = stringResource(R.string.total_label),
				style = MaterialTheme.typography.bodySmall,
				color = displayColor,
				textAlign = TextAlign.Center
			)
		}
	}

	LazyRow(
		modifier = Modifier
			.fillMaxWidth()
			.padding(
				horizontal = 16.dp,
				vertical = 16.dp
			),
		horizontalArrangement = Arrangement.spacedBy(8.dp)
	) {
		data.forEachIndexed { index, item ->
			item(key = item.category.id) {
				CategoryCard(
					category = item.category,
					color = MaterialTheme.colorScheme.onPrimary,
					backgroundColor = item.color
				)
			}
		}
	}
}