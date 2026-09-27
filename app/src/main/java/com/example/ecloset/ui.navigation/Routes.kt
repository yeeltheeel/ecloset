package com.example.ecloset.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.example.ecloset.ui.screens.main.MainPage
import com.example.ecloset.ui.screens.main.AnalyticsPage
import com.example.ecloset.ui.screens.closet.NewItemPage
import com.example.ecloset.ui.screens.outfits.NewOutfitPage

object Routes {
	const val MAIN = "main"
	const val ANALYTICS = "analytics"

	const val ADD_ITEM = "add_clothing"
	const val ADD_OUTFIT = "add_outfit"

	const val CLOSET = "closet"
	const val OUTFITS = "outfits"
}

@Composable
fun NavActionHost(
	navController: NavHostController,
	innerPadding: PaddingValues,
	modifier: Modifier
) {
	NavHost(
		navController = navController,
		startDestination = Routes.MAIN,
		modifier = modifier
	) {
		composable(Routes.MAIN) {
			MainPage(modifier = modifier)
		}
		composable(Routes.ANALYTICS) {
			AnalyticsPage(modifier = modifier)
		}
		composable(Routes.ADD_ITEM) {
			NewItemPage(modifier = modifier)
		}
		composable(Routes.ADD_OUTFIT) {
			NewOutfitPage(modifier = modifier)
		}
	}
}
