package com.example.ecloset.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ecloset.user.data.Presets.ColorSelect
import com.example.ecloset.user.local.CategoryItem
import com.example.ecloset.user.local.PreviewCardData

@Composable
fun GridLayout(
	itemList: List<PreviewCardData>
){
	LazyVerticalGrid(
		columns = GridCells.Adaptive(minSize = 100.dp),
		modifier = Modifier
			.fillMaxWidth()
			.heightIn(max = 1000.dp),
		contentPadding = PaddingValues(16.dp),
		horizontalArrangement = Arrangement.spacedBy(8.dp),
		verticalArrangement = Arrangement.spacedBy(8.dp)
	) {
		if (itemList.isNotEmpty()) {
			items(
				items = itemList,
				key = { card -> card.id }
			) { card ->
				PreviewGridCard(
					data = card
				)
			}
		} else {
			item{
				EmptyPreviewCard()
			}
		}
	}
}

@Composable
fun GridHeader(
	categoryList: List<CategoryItem>
){
	LazyRow(
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = 16.dp),
		horizontalArrangement = Arrangement.spacedBy(8.dp)
	) {
		items(
			items = categoryList,
			key = { category -> category.id }
		) { category ->
			CategoryCard(category = category)
		}
	}
}

@Composable
fun MultiSelectColorGrid(
	title: String,
	itemList: List<ColorSelect>,
	selectedIds: List<Int>,
	onSelected: (List<Int>) -> Unit
) {
	Text(
		text = title,
		modifier = Modifier
			.fillMaxWidth()
			.padding(
				horizontal = 16.dp,
				vertical = 8.dp
			),
		style = MaterialTheme.typography.bodyLarge
	)

	LazyVerticalGrid(
		columns = GridCells.Adaptive(minSize = 70.dp),
		modifier = Modifier
			.fillMaxWidth()
			.heightIn(max = 1000.dp),
		contentPadding = PaddingValues(16.dp),
		horizontalArrangement = Arrangement.spacedBy(8.dp),
		verticalArrangement = Arrangement.spacedBy(8.dp),
	) {
		items(
			items = itemList,
			key = { color -> color.id }
		) { color ->
			val isSelected = color.id in selectedIds
			Card(
				modifier = Modifier
					.width(60.dp)
					.aspectRatio(1f),
				colors = CardDefaults.cardColors(
					containerColor = color.value,
					contentColor = if (color.value.luminance() <= 0.5f)
						MaterialTheme.colorScheme.background
					else
						MaterialTheme.colorScheme.onBackground
				),
				elevation = CardDefaults.cardElevation(
					defaultElevation = 2.dp
				),
				onClick = {
					onSelected(
						if (isSelected)
							selectedIds - color.id
						else
							selectedIds + color.id
					)
				}
			){
//				Text(
//					text = color.name,
//					style = MaterialTheme.typography.bodySmall,
//					textAlign = TextAlign.Center,
//					modifier = Modifier
//						.padding(8.dp)
//				)
				if (isSelected){
					Icon(
						imageVector = Icons.Default.CheckCircleOutline,
						contentDescription = null,
						modifier = Modifier
							.padding(8.dp)
					)
				}
			}
		}
	}
}
