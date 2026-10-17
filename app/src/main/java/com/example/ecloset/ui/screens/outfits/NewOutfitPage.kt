package com.example.ecloset.ui.screens.outfits

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ecloset.R
import com.example.ecloset.ui.components.OutfitCollageCanvas
import com.example.ecloset.ui.components.PageHeader
import com.example.ecloset.ui.components.RadioGroupBlock
import com.example.ecloset.user.data.ClosetItemData

@Composable
fun NewOutfitPage(
	modifier: Modifier = Modifier,
	navController: NavHostController
){
	val occasionList = listOf("Casual", "Party", "Business")
	var occasion by remember { mutableIntStateOf(0) }

	val locationList = listOf("Inside", "Outside", "Both")
	var location by remember { mutableIntStateOf(0) }

	var colorList = listOf("Green", "Brown", "Pink", "Purple", "Red", "Yellow", "Blue", "White", "Black", "Grey")
	var color by remember { mutableIntStateOf(0) }

	LazyColumn(
		modifier
			.fillMaxWidth(),
		contentPadding = PaddingValues(top = 0.dp)
	) {
		item{
			PageHeader(
				title = stringResource(R.string.new_outfit_title),
				navController = navController
			)
		}
		item{
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
		item{
			SelectBlock(
				title = stringResource(R.string.color_prompt),
				itemList = colorList,
				selectedIndex = 0,
				onSelectedIndex = { index ->
					color = index
				}
			)
		}
		item{
			// get item suggestions
		}
		item{
			// load canvas
			OutfitCollageCanvas(
				selectedItems = emptyList<ClosetItemData>()
			)
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
			)
			.clickable{},
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
