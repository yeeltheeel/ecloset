package com.example.ecloset.ui.screens.main

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.ecloset.ui.components.PreviewCarousel
import com.example.ecloset.R
import com.example.ecloset.ui.components.WeatherWidget
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.ecloset.ui.navigation.Routes
import com.example.ecloset.user.local.MockData.previewOutfits
import com.example.ecloset.user.local.MockData.previewClothes
import com.example.ecloset.user.local.UserViewModel

@Composable
fun MainPage(
	modifier: Modifier = Modifier,
	navController: NavHostController,
	userViewModel: UserViewModel
) {
	val state by userViewModel.userState.collectAsStateWithLifecycle()
	val user = state.user

	LazyColumn(
		modifier = modifier
			.fillMaxWidth(),
		contentPadding = PaddingValues(top = 0.dp)
	) {
		item {
			WeatherWidget(
				modifier = modifier,
				location = user?.location?.location ?: "",
				latitude = user?.location?.latitude ?: .0,
				longitude = user?.location?.longitude ?: .0
			)
		}
		item {
			PreviewCarousel(
				previewTitle = stringResource(R.string.outfit_list_title) + " >",
				cards = previewOutfits,
				navController = navController,
				currentRoute = Routes.OUTFITS
			)
		}
		item {
			PreviewCarousel(
				previewTitle = stringResource(R.string.clothes_list_title) + " >",
				cards = previewClothes,
				navController = navController,
				currentRoute = Routes.CLOSET
			)
		}
	}
}