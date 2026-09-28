package com.example.ecloset.ui.screens.main

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.ecloset.ui.components.PreviewCarousel
import com.example.ecloset.R
import com.example.ecloset.ui.components.PreviewCard
import com.example.ecloset.ui.components.WeatherWidget
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ecloset.ui.navigation.Routes

@Composable
fun MainPage(
	modifier: Modifier = Modifier,
	navController: NavHostController
) {
	/* begin test data */
	val previewOutfits = listOf(
		PreviewCard(
			id = 1,
			title = "Outfit 1",
			color = MaterialTheme.colorScheme.onTertiary
		),
		PreviewCard(
			id = 2,
			title = "Outfit 2",
			color = MaterialTheme.colorScheme.onTertiary
		),
		PreviewCard(
			id = 3,
			title = "Outfit 3",
			color = MaterialTheme.colorScheme.onTertiary
		)
	)

	val previewClothes = listOf(
		PreviewCard(
			id = 1,
			title = "Item 1",
			color = MaterialTheme.colorScheme.onTertiary
		),
		PreviewCard(
			id = 2,
			title = "Item 2",
			color = MaterialTheme.colorScheme.onTertiary
		),
		PreviewCard(
			id = 3,
			title = "Item 3",
			color = MaterialTheme.colorScheme.onTertiary
		)
	)
	/* end test data */

	LazyColumn(
		modifier = modifier
			.fillMaxWidth(),
			contentPadding = PaddingValues(top = 0.dp)
	) {
		item {
			WeatherWidget(modifier = modifier, location = "Moscow")
		}
		item {
			PreviewCarousel(
				modifier = modifier,
				previewTitle = stringResource(R.string.outfit_list_title) + " >",
				cards = previewOutfits,
				onCardClick = { },
				navController = navController,
				currentRoute = Routes.OUTFITS
			)
		}
		item {
			PreviewCarousel(
				modifier = modifier,
				previewTitle = stringResource(R.string.clothes_list_title) + " >",
				cards = previewClothes,
				onCardClick = {},
				navController = navController,
				currentRoute = Routes.CLOSET
			)
		}
	}
}