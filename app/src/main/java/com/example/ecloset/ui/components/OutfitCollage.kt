package com.example.ecloset.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.ecloset.R
import com.example.ecloset.user.data.Presets.clothesCategories
import com.example.ecloset.user.local.CategoryItem
import com.example.ecloset.user.local.ClosetItemData
import com.example.ecloset.user.local.OutfitItemData
import kotlin.math.roundToInt

data class CollageEntry(
	val id: Int,
	val offset: Offset,
)

@Composable
fun OutfitCollageCanvas(
	categoryList: List<CategoryItem> = clothesCategories,
	selectedItems: List<ClosetItemData>
) {
	var currentOutfit by remember {
		mutableStateOf<List<ClosetItemData>>(emptyList())
	}
	var positions by remember {
		mutableStateOf<Map<Int, Offset>>(emptyMap())
	}
	val currentIds = remember(currentOutfit) {
		currentOutfit.map { it.id }.toSet()
	}

	fun toggle(item: ClosetItemData) {
		currentOutfit = if (item.id in currentIds) {
			currentOutfit.filterNot { it.id == item.id }.also {
				positions = positions - item.id // drop its layout
			}
		} else {
			currentOutfit + item // append -> defaults to center
		}
	}

	val maxSize = 250.dp
	var canvasSize by remember { mutableStateOf(IntSize.Zero) }

	Column(
		modifier = Modifier
			.fillMaxWidth(),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Box(
			modifier = Modifier
				.sizeIn(maxWidth = maxSize, maxHeight = maxSize)
				.aspectRatio(1f)
		) {
			Canvas(
				modifier = Modifier
					.fillMaxSize()
					.background(MaterialTheme.colorScheme.surfaceVariant)
					.clip(RoundedCornerShape(16.dp))
			) {
				// background drawing
			}

			Box(
				modifier = Modifier
					.matchParentSize()
					.clip(RoundedCornerShape(16.dp))
					.onSizeChanged { canvasSize = it }
			) {
				currentOutfit.forEach { item ->
					key(item.id) {
						DraggableImage(
							item = item,
							position = positions[item.id] ?: Offset.Zero,
							canvasSize = canvasSize,
							onDragEnd = { delta ->
								val cur = positions[item.id] ?: Offset.Zero
								val halfW = canvasSize.width / 2f
								val halfH = canvasSize.height / 2f
								val next = cur + delta
								positions = positions + (item.id to Offset(
									x = next.x.coerceIn(-halfW + COLLAGE_HALF, halfW - COLLAGE_HALF),
									y = next.y.coerceIn(-halfH + COLLAGE_HALF, halfH - COLLAGE_HALF)
								))
							}
						)
					}
				}
			}
		}
	}

	Box(
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp)
	) {
		Column(modifier = Modifier.fillMaxWidth()) {
			categoryList.forEach { category ->
				key(category.id) {
					OutfitCollageTab(
						category = category,
						selectedCategoryItems = selectedItems.filter {
							it.category == category.id
						},
						selectedIds = currentIds,
						onToggle = ::toggle
					)
				}
			}
		}
	}

	Button(
		modifier = Modifier
			.width(300.dp)
			.padding(
				horizontal = 16.dp,
				vertical = 8.dp
			),
		onClick = {
			// call repo
		}
	) {
		Text(
			text = stringResource(R.string.add_prompt) +
					" " +
					stringResource(R.string.outfit_text),
			modifier = Modifier
				.fillMaxWidth()
				.padding(
					horizontal = 12.dp,
					vertical = 8.dp
				),
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onPrimary,
			textAlign = TextAlign.Center,
			maxLines = 1
		)
	}
}

@Composable
fun OutfitCollageTab(
	category: CategoryItem,
	selectedCategoryItems: List<ClosetItemData>,
	selectedIds: Set<Int>,
	onToggle: (ClosetItemData) -> Unit
){
	GroupSubTitle(
		title = stringResource(category.titleId)
	)
	LazyRow(
		modifier = Modifier
			.padding(
				vertical = 4.dp
			)
			.fillMaxWidth(),
		horizontalArrangement = Arrangement.spacedBy(8.dp)
	) {
		if (selectedCategoryItems.isNotEmpty()) {
			items(
				items = selectedCategoryItems,
				key = { item -> item.id }
			) { item ->
				SelectCard(
					data = item,
					selected = item.id in selectedIds,
					onClick = { onToggle(item) }
				)
			}
		} else {
			item {
				EmptySelectCard()
			}
		}
	}
}

@Composable
fun SelectCard(
	data: ClosetItemData,
	selected: Boolean,
	onClick: () -> Unit
){
	Card(
		onClick = {
			// add to canvas
			onClick()
		},
		modifier = Modifier
			.width(100.dp)
			.height(100.dp),
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.surfaceVariant,
			contentColor = MaterialTheme.colorScheme.onSurfaceVariant
		),
		elevation = CardDefaults.cardElevation(
			defaultElevation = 2.dp
		)
	){
		val previewModel = data.imageUrl ?: R.drawable.hanger
		val tintVar = (data.imageUrl == null)
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.fillMaxHeight()
				.padding(8.dp),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center
		) {
			AsyncImage(
				model = previewModel,
				contentDescription = "preview image",
				contentScale = ContentScale.Fit,
				colorFilter = if (tintVar) {
					ColorFilter.tint(MaterialTheme.colorScheme.tertiary)
				} else {
					null
				},
				modifier = Modifier
					.height(130.dp)
					.padding(16.dp)
			)
		}
	}
}

@Composable
fun EmptySelectCard(){
	Card(
		onClick = {},
		modifier = Modifier
			.width(100.dp)
			.height(100.dp),
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.surfaceVariant,
			contentColor = MaterialTheme.colorScheme.onSurfaceVariant
		),
		elevation = CardDefaults.cardElevation(
			defaultElevation = 2.dp
		)
	){
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.fillMaxHeight()
				.padding(8.dp),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center
		) {
			Text(
				text = stringResource(R.string.no_content),
				style = MaterialTheme.typography.titleSmall,
				color = MaterialTheme.colorScheme.tertiary,
				maxLines = 1
			)
		}
	}
}

private const val COLLAGE_ITEM_DP = 90
private const val COLLAGE_HALF = 45f

@Composable
private fun DraggableImage(
	item: ClosetItemData,
	position: Offset,
	canvasSize: IntSize,
	onDragEnd: (Offset) -> Unit
) {
	var drag by remember { mutableStateOf(Offset.Zero) }
	val halfW = canvasSize.width / 2f
	val halfH = canvasSize.height / 2f
	val density = LocalDensity.current
	val sizePx = with(density) { COLLAGE_ITEM_DP.dp.toPx() }
	val halfPx = sizePx / 2f

	val previewModel = item.imageUrl ?: R.drawable.hanger
	val isPlaceholder = item.imageUrl == null

	AsyncImage(
		model = previewModel,
		contentDescription = "collage item",
		contentScale = ContentScale.Fit,
		colorFilter = if (isPlaceholder)
			ColorFilter.tint(MaterialTheme.colorScheme.tertiary) else null,
		modifier = Modifier
			.offset {
				IntOffset(
					x = (halfW + position.x + drag.x - halfPx).roundToInt(),
					y = (halfH + position.y + drag.y - halfPx).roundToInt()
				)
			}
			.size(COLLAGE_ITEM_DP.dp)
			.pointerInput(Unit) {
				detectDragGestures(
					onDragEnd = {
						if (drag != Offset.Zero) {
							onDragEnd(drag)
							drag = Offset.Zero
						}
					},
					onDragCancel = { drag = Offset.Zero },
					onDrag = { change, d ->
						change.consume()
						drag += d
					}
				)
			}
	)
}
