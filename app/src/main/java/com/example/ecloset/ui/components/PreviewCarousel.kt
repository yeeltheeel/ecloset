package com.example.ecloset.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ecloset.user.local.PreviewCard

@Composable
fun PreviewCarousel(
	previewTitle: String,
	cards: List<PreviewCard>,
	navController: NavHostController,
	currentRoute: String
){
	Text(
		text = previewTitle,
		modifier = Modifier
			.fillMaxWidth()
			.clickable {
				navController.navigate(currentRoute)
			}
			.padding(
				horizontal = 16.dp,
				vertical = 12.dp
			),
		style = MaterialTheme.typography.titleLarge,
		color = MaterialTheme.colorScheme.primary
	)

	LazyRow(
		modifier = Modifier
			.fillMaxWidth(),
		contentPadding = PaddingValues(
			horizontal = 16.dp,
			vertical = 16.dp
		),
		horizontalArrangement = Arrangement.spacedBy(12.dp)
	) {
		items(
			items = cards,
			key = { card -> card.id }
		) { card ->
			PreviewGridCard(
				data = card
			)
		}
	}
}

@Composable
fun PreviewSingleRow(
	cards: List<PreviewCard>
){
	LazyRow(
		modifier = Modifier
			.fillMaxWidth(),
		contentPadding = PaddingValues(
			horizontal = 16.dp,
			vertical = 16.dp
		),
		horizontalArrangement = Arrangement.spacedBy(12.dp)
	) {
		items(
			items = cards,
			key = { card -> card.id }
		) { card ->
			PreviewGridCard(
				data = card
			)
		}
	}
}
