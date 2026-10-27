package com.example.ecloset.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ecloset.user.data.PreviewCard
import com.example.ecloset.ui.components.PreviewGridCard

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
