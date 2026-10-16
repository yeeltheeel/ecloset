package com.example.ecloset.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ecloset.user.data.CategoryItem
import com.example.ecloset.user.data.ClosetItemData
import com.example.ecloset.user.data.PreviewCardData

// merge it with categoryItem later
data class DonutChartData(
	val category: CategoryItem,
	val units: Int,
	val percent: Float,
	val color: Color
)

@Composable
fun DonutChart(
	categories: List<CategoryItem>,
	items: List<PreviewCardData>
){
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
	//val titles = categories.map { stringResource(it.titleId) }

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

	Box(
		modifier = Modifier
			.fillMaxWidth()
			.padding(
				horizontal = 50.dp,
				vertical = 8.dp
			),
		contentAlignment = Alignment.Center
	) {
		val strokeWidth = 50.dp
		val trackColor = MaterialTheme.colorScheme.surface
		val maxSize = 250.dp
		val cornerRadiusPx = with(LocalDensity.current) { 4.dp.toPx() }

		Canvas(
			modifier = Modifier
				.sizeIn(
					maxWidth = maxSize,
					maxHeight = maxSize
				)
				.aspectRatio(1f)
		) {
			val strokePx = strokeWidth.toPx()
			val diameter = size.minDimension - strokePx
			val arcSize = Size(diameter, diameter)
			val topLeft = Offset(
				(size.width - diameter) / 2f,
				(size.height - diameter) / 2f,
			)

			val radius = diameter / 2f
			val gapDeg = 4f // gap between segments, in degrees
			val capAngleDeg = if (radius > 0f) {
				(strokePx / 2f) / radius * (180f / Math.PI.toFloat())
			} else 0f // angular half-width of the rounded cap
			var startAngle = -90f

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
		data.forEach { item ->
			item{
				CategoryCard(
					category = item.category,
					color = MaterialTheme.colorScheme.onPrimary,
					backgroundColor = item.color
				)
			}
		}
	}
}
