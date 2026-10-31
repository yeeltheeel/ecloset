package com.example.ecloset.ui.screens.main

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.ecloset.ui.components.PreviewCarousel
import com.example.ecloset.R
import com.example.ecloset.ui.components.WeatherWidget
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.ecloset.ui.navigation.Routes
import com.example.ecloset.user.local.viewmodels.HomeViewmodel
import com.example.ecloset.user.local.viewmodels.UserViewModel

@Composable
fun MainPage(
	modifier: Modifier = Modifier,
	navController: NavHostController,
	userViewModel: UserViewModel,
	homeViewmodel: HomeViewmodel = hiltViewModel()
) {
	val state by userViewModel.userState.collectAsStateWithLifecycle()
	val location = state.user?.location // LocationInfo("", .0, .0)

	val previewClothesState by homeViewmodel.previewClothes.collectAsStateWithLifecycle()
	val previewOutfitsState by homeViewmodel.previewOutfits.collectAsStateWithLifecycle()

	LazyColumn(
		modifier = modifier
			.fillMaxWidth(),
		contentPadding = PaddingValues(top = 0.dp)
	) {
		item {
			WeatherWidget(
				modifier = modifier,
				location = location?.location ?: "",
				latitude = location?.latitude ?: .0,
				longitude =location?.longitude ?: .0
			)
		}
		item {
			PreviewCarousel(
				previewTitle = stringResource(R.string.outfit_list_title) + " >",
				cards = previewOutfitsState.items,
				navController = navController,
				currentRoute = Routes.OUTFITS
			)
		}
		item {
			PreviewCarousel(
				previewTitle = stringResource(R.string.clothes_list_title) + " >",
				cards = previewClothesState.items,
				navController = navController,
				currentRoute = Routes.CLOSET
			)
		}
		item{
			Spacer(modifier = Modifier.padding(vertical = 70.dp))
		}
	}
}