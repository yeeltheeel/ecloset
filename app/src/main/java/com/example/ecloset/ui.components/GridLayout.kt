package com.example.ecloset.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ecloset.user.data.CategoryItem

@Composable
fun GridLayout(
	categoryList: List<CategoryItem>,
	itemList: List<PreviewCard>
){
	LazyRow(
		Modifier
			.fillMaxWidth()
	) {
		items(
			items = categoryList,
			key = { card -> card.id }
		) { card ->
			Card() {
				Text(
					text = card.title
				)
			}
		}
	}
	LazyVerticalGrid(
		columns = GridCells.Adaptive(100.dp),
		content = {
			items(
				items = itemList,
				key = { card -> card.id }
			) { card ->
				PreviewOutfitCard(
					card = card,
					onClick = {
					}
				)
			}
		}
	)
}
