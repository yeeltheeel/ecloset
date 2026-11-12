package com.example.ecloset.ui.screens.outfits

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.ecloset.R
import com.example.ecloset.ui.components.DetailsPopup
import com.example.ecloset.ui.components.MultiSelectColorGrid
import com.example.ecloset.ui.components.OutfitCollageCanvas
import com.example.ecloset.ui.components.PageHeader
import com.example.ecloset.ui.components.RadioGroupBlock
import com.example.ecloset.user.local.ClosetItemData
import com.example.ecloset.user.data.Presets.ColorSelect
import com.example.ecloset.user.data.Presets.colorList
import com.example.ecloset.user.local.viewmodels.NewOutfitViewmodel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewOutfitPage(
	modifier: Modifier = Modifier,
	navController: NavHostController,
	newOutfitViewmodel: NewOutfitViewmodel = hiltViewModel()
){
	val newOutfitState by newOutfitViewmodel.clothesList.collectAsStateWithLifecycle()
	var selectedItems by remember { mutableStateOf(newOutfitState.items) }
	var showFilters by remember { mutableStateOf(false) }

	Box(
		modifier = modifier.fillMaxSize()
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
				OutfitCollageCanvas(
					selectedItems = selectedItems
				)
			}
			item {
				Spacer(
					modifier = Modifier
						.padding(vertical = 120.dp)
				)
			}
		}
	}

	Box(
		modifier = Modifier
			.fillMaxSize()
			.padding(bottom = 150.dp),
		contentAlignment = Alignment.BottomCenter
	){
		FloatingActionButton(
			modifier = Modifier
				.padding(
					horizontal = 12.dp,
					vertical = 8.dp
				)
				.width(300.dp)
				.wrapContentHeight()
				.clip(RoundedCornerShape(32.dp)),
			containerColor = MaterialTheme.colorScheme.tertiary,
			onClick = { showFilters = true },
		) {
			Text(
				modifier = Modifier
					.padding(0.dp),
				text = stringResource(R.string.get_suggestions_prompt),
				color = MaterialTheme.colorScheme.onTertiary
			)
		}
	}

	if (showFilters){
		DetailsPopup(
			content = {
				QuizBlock(newOutfitViewmodel, {})
			},
			onDismiss = {
				showFilters = false
			}
		)
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
fun QuizBlock(
	newOutfitViewmodel: NewOutfitViewmodel,
	onClick: () -> Unit
){
	val occasionList = listOf("Casual", "Party", "Business")
	var occasion by remember { mutableIntStateOf(0) }
	val locationList = listOf("Inside", "Outside", "Both")
	var location by remember { mutableIntStateOf(0) }
	var colors by remember { mutableStateOf(emptyList<ColorSelect>()) }

	Column(
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		SelectBlock(
			title = stringResource(R.string.occasion_prompt),
			itemList = occasionList,
			selectedIndex = occasion,
			onSelectedIndex = { index ->
				occasion = index
			}
		)
		SelectBlock(
			title = stringResource(R.string.location_prompt),
			itemList = locationList,
			selectedIndex = location,
			onSelectedIndex = { index ->
				location = index
			}
		)
		var selected by remember {
			mutableStateOf<List<Int>>(emptyList())
		}
		MultiSelectColorGrid(
			title = stringResource(R.string.color_prompt),
			itemList = colorList,
			selectedIds = selected,
			onSelected = { selected = it }
		)
		// get item suggestions
		Button(
			modifier = Modifier
				.width(300.dp)
				.padding(
					horizontal = 16.dp,
					vertical = 8.dp
				),
			onClick = {
				onClick()
			}
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
