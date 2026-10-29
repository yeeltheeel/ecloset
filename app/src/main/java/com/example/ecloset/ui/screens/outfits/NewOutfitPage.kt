package com.example.ecloset.ui.screens.outfits

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ecloset.R
import com.example.ecloset.ui.components.OutfitCollageCanvas
import com.example.ecloset.ui.components.PageHeader
import com.example.ecloset.ui.components.PreviewGridCard
import com.example.ecloset.ui.components.RadioGroupBlock
import com.example.ecloset.user.data.MockData.clothesList
import com.example.ecloset.user.local.ClosetItemData
import com.example.ecloset.user.data.Presets.ColorSelect
import com.example.ecloset.user.data.Presets.colorList

@Composable
fun NewOutfitPage(
	modifier: Modifier = Modifier,
	navController: NavHostController
){
	val occasionList = listOf("Casual", "Party", "Business")
	var occasion by remember { mutableIntStateOf(0) }

	val locationList = listOf("Inside", "Outside", "Both")
	var location by remember { mutableIntStateOf(0) }

	var colors by remember { mutableStateOf(emptyList<ColorSelect>()) }

	var selectedItems = clothesList

	Box(
		modifier = modifier
			.fillMaxSize()
	) {
		LazyColumn(
			modifier
				.fillMaxWidth(),
			contentPadding = PaddingValues(top = 0.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			item {
				PageHeader(
					title = stringResource(R.string.new_outfit_title),
					navController = navController
				)
			}
			item {
				SelectBlock(
					title = stringResource(R.string.occasion_prompt),
					itemList = occasionList,
					selectedIndex = 0,
					onSelectedIndex = { index ->
						occasion = index
					}
				)
			}
			item {
				SelectBlock(
					title = stringResource(R.string.location_prompt),
					itemList = locationList,
					selectedIndex = 0,
					onSelectedIndex = { index ->
						location = index
					}
				)
			}
			item {
				MultiSelectColorGrid(
					title = stringResource(R.string.color_prompt),
					itemList = colorList,
					onSelected = { indexes ->
						colors = indexes.map { colorList[it] }
					}
				)
			}
			item {
				// get item suggestions
				Button(
					modifier = Modifier
						.width(300.dp)
						.padding(
							horizontal = 16.dp,
							vertical = 8.dp
						),
					onClick = {},
				) {
					Text(
						text = stringResource(R.string.get_items_prompt),
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
			item {
				// load canvas
				OutfitCollageCanvas(
					selectedItems = selectedItems
				)
			}
		}
	}
}

@Composable
fun SelectBlock(
	title: String,
	itemList: List<String>,
	selectedIndex: Int,
	onSelectedIndex: (Int) -> Unit
){
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

	RadioGroupBlock(
		options = itemList, // will add overload for different types
		selectedIndex = selectedIndex,
		onOptionSelected = {index ->
			onSelectedIndex(index)
		}
	)
}

@Composable
fun MultiSelectBlock(
	title: String,
	itemList: List<String>,
	onSelectedIndex: (List<Int>) -> Unit
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

	// checkbox list
}

@Composable
fun MultiSelectColorGrid(
	title: String,
	itemList: List<ColorSelect>,
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

	val selected = remember { mutableStateListOf<Int>() }

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
					selected.add(color.id)
				}
			){
				Text(
					text = color.name,
					style = MaterialTheme.typography.bodySmall,
					textAlign = TextAlign.Center,
					modifier = Modifier
						.padding(8.dp)
				)
			}
		}
	}
}

fun filterItems(
	itemList: List<ClosetItemData>,
	occasion: String? = null,
	colors: List<Int> = emptyList()
) : MutableList<ClosetItemData> {
	return itemList
		.asSequence()
		.filter { occasion == null || it.occasion == occasion }
		.filter { colors.isEmpty() || it.color in colors }
		.toMutableList()
}
