package com.example.ecloset.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.example.ecloset.ui.screens.main.MainPage
import com.example.ecloset.ui.screens.main.AnalyticsPage
import com.example.ecloset.ui.screens.closet.NewItemPage
import com.example.ecloset.ui.screens.outfits.NewOutfitPage
import com.example.ecloset.ui.screens.closet.ClosetPage
import com.example.ecloset.ui.screens.outfits.OutfitPage
import com.example.ecloset.ui.screens.user.UserProfile
import com.example.ecloset.ui.screens.user.AboutPage
import com.example.ecloset.ui.screens.auth.LoginPage
import com.example.ecloset.ui.screens.auth.RegistrationPage
import com.example.ecloset.user.data.UserViewModel

object Routes {
	const val MAIN = "main"
	const val ANALYTICS = "analytics"

	const val ADD_ITEM = "add_clothing"
	const val ADD_OUTFIT = "add_outfit"

	const val CLOSET = "closet"
	const val OUTFITS = "outfits"

	const val USER_SETTINGS = "user_profile"
	const val ABOUT = "about"

	const val LOGIN = "login"
	const val REGISTER = "registration"
}

@Composable
fun NavActionHost(
	modifier: Modifier,
	navController: NavHostController,
	innerPadding: PaddingValues,
	userViewModel: UserViewModel
) {
	val userState by userViewModel.userState.collectAsState()
	val startDest = if (userState.loggedIn) Routes.MAIN else Routes.LOGIN

	NavHost(
		navController = navController,
		startDestination = startDest,
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
				navController = navController,
				userViewModel = userViewModel
			)
		}
		composable(Routes.ABOUT) {
			AboutPage(
				modifier = modifier,
				navController = navController
			)
		}
		composable(Routes.LOGIN) {
			LoginPage(
				modifier = modifier,
				navController = navController
			)
		}
		composable(Routes.REGISTER) {
			RegistrationPage(
				modifier = modifier,
				navController = navController,
			)
		}
	}
}
