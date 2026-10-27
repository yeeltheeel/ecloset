package com.example.ecloset.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ecloset.user.data.CategoryItem

// FilterDetails -> selectedFilters -> LazyRow

@Composable
fun FilterBlock(
	modifier: Modifier,
	filterList: List<CategoryItem>
){
	var selectedFilters by remember { mutableStateOf(emptyList<CategoryItem>()) }

	Icon(
		modifier = Modifier
			.clickable{
				// open filter bottom sheet
			}
			.padding(
				horizontal = 8.dp
			),
		imageVector = Icons.Filled.FilterList,
		contentDescription = "Open filters",
		tint = MaterialTheme.colorScheme.primary
	)

	LazyRow(
		modifier = Modifier
			.fillMaxWidth()
			.padding(
				horizontal = 16.dp,
				vertical = 8.dp
			)
	){
		selectedFilters.forEach{ filter ->
			// filter card category style
		}
	}
}

@Composable
fun FilterDetails(
	modifier: Modifier
){
	// filters
	// color
	// occasion
	// season
}
