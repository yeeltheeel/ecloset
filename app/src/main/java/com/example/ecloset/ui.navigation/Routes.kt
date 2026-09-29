package com.example.ecloset.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ecloset.ui.screens.user.UserProfile

import com.example.ecloset.ui.screens.main.MainPage
import com.example.ecloset.ui.screens.main.AnalyticsPage
import com.example.ecloset.ui.screens.closet.NewItemPage
import com.example.ecloset.ui.screens.outfits.NewOutfitPage
import com.example.ecloset.ui.screens.closet.ClosetPage
import com.example.ecloset.ui.screens.outfits.OutfitPage
import com.example.ecloset.ui.screens.user.AboutPage

object Routes {
	const val MAIN = "main"
	const val ANALYTICS = "analytics"

	const val ADD_ITEM = "add_clothing"
	const val ADD_OUTFIT = "add_outfit"

	const val CLOSET = "closet"
	const val OUTFITS = "outfits"

	const val USER_SETTINGS = "user_profile"
	const val ABOUT = "about"
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
			MainPage(
				modifier = modifier,
				navController = navController
			)
		}
		composable(Routes.ANALYTICS) {
			AnalyticsPage(
				modifier = modifier
			)
		}
		composable(Routes.ADD_ITEM) {
			NewItemPage(
				modifier = modifier,
				navController = navController
			)
		}
		composable(Routes.ADD_OUTFIT) {
			NewOutfitPage(
				modifier = modifier,
				navController = navController
			)
		}
		composable(Routes.CLOSET) {
			ClosetPage(
				modifier = modifier,
				navController = navController
			)
		}
		composable(Routes.OUTFITS) {
			OutfitPage(
				modifier = modifier,
				navController = navController
			)
		}
		composable(Routes.USER_SETTINGS) {
			UserProfile(
				modifier = modifier,
				navController = navController
			)
		}
		composable(Routes.ABOUT) {
			AboutPage(
				modifier = modifier,
				navController = navController
			)
		}
	}
}
