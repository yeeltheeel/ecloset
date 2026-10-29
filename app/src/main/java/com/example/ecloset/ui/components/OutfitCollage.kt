package com.example.ecloset.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ecloset.R
import com.example.ecloset.user.data.Presets.clothesCategories
import com.example.ecloset.user.local.CategoryItem
import com.example.ecloset.user.local.ClosetItemData
import com.example.ecloset.user.local.OutfitItemData

@Composable
fun OutfitCollageCanvas(
	categoryList: List<CategoryItem> = clothesCategories,
	selectedItems: List<ClosetItemData>
){
	val maxSize = 250.dp

	Canvas(
		modifier = Modifier
			.sizeIn(
				maxWidth = maxSize,
				maxHeight = maxSize
			)
			.aspectRatio(1f)
	){}

	Box(
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp)
	){
		LazyVerticalGrid(
			columns = GridCells.Adaptive(minSize = 70.dp),
			modifier = Modifier
				.fillMaxWidth()
				.heightIn(max = 1000.dp),
			contentPadding = PaddingValues(16.dp),
			horizontalArrangement = Arrangement.spacedBy(8.dp),
			verticalArrangement = Arrangement.spacedBy(8.dp)
		) {
			items(
				items = selectedItems,
				key = { item -> item.id }
			) { item ->
				Card(
					modifier = Modifier
						.width(60.dp)
						.aspectRatio(1f),
					colors = CardDefaults.cardColors(
						containerColor = MaterialTheme.colorScheme.surfaceVariant,
						contentColor = MaterialTheme.colorScheme.onSurfaceVariant
					),
					elevation = CardDefaults.cardElevation(
						defaultElevation = 2.dp
					),
					onClick = {}
				){
					Text(
						text = item.id.toString(),
						style = MaterialTheme.typography.bodySmall,
						textAlign = TextAlign.Center,
						modifier = Modifier
							.padding(8.dp)
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
		onClick = {}
	) {
		Text(
			text = stringResource(com.example.ecloset.R.string.add_prompt) +
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
