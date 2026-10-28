package com.example.ecloset.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ecloset.user.local.CategoryItem
import com.example.ecloset.user.local.PreviewCard

@Composable
fun GridLayout(
	itemList: List<PreviewCard>
){
	LazyVerticalGrid(
		columns = GridCells.Adaptive(minSize = 100.dp),
		modifier = Modifier
			.fillMaxWidth()
			.heightIn(max = 1000.dp),
		contentPadding = PaddingValues(16.dp),
		horizontalArrangement = Arrangement.spacedBy(8.dp),
		verticalArrangement = Arrangement.spacedBy(8.dp),
	) {
		items(
			items = itemList,
			key = { card -> card.id }
		) { card ->
			PreviewGridCard(
				data = card
			)
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

