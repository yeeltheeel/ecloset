@file:OptIn(ExperimentalMaterial3Api::class)
@file:SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")

/* TODO:
	main:
		functions:
			closet view models
			connect backend:
				dto
				tokens
				enums
			call weather from backend
			implement user theme preference (light/dark/system, static/dynamic)
			filters for outfits/clothing (with some presets: category for items, year/occasion for outfits)
			last added items, relevant outfits
			unused clothes
		widgets:
			user value update (text -> input)
			outfit collage
			location select
			filters block
			filters page
			add user tint selecting block to user settings
			prettify weather widget with lottie
	medium:
		add localizations for all hardcoded strings
		donut chart placeholder if itemList empty
		clear up paddings for consistency
	misc:
		add admin/support functions
*/

package com.example.ecloset

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.ecloset.ui.theme.EClosetTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ecloset.ui.components.ActionBarMenu
import com.example.ecloset.ui.components.Header
import androidx.navigation.compose.rememberNavController
import com.example.ecloset.ui.navigation.NavActionHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.ecloset.ui.navigation.Routes
import com.example.ecloset.ui.screens.main.NoConnectionPage
import com.example.ecloset.user.local.ConnectionState
import com.example.ecloset.user.local.viewmodels.ConnectionViewModel
import com.example.ecloset.user.local.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		window.isNavigationBarContrastEnforced = false
		setContent {
			EClosetTheme {
				EclosetApp()
			}
		}
	}
}

@Composable
fun EclosetApp() {
	val connectionViewModel: ConnectionViewModel = viewModel()
	val connectionState by connectionViewModel.state.collectAsState()

	when (val state = connectionState) {
		ConnectionState.Loading -> EclosetScreen(
			connectionViewModel = connectionViewModel
		) // mb will be splash
		ConnectionState.Offline -> NoConnectionPage(
			modifier = Modifier
				.fillMaxSize()
				.windowInsetsPadding(WindowInsets.systemBars),
			onRetry = {
				connectionViewModel.retry()
			}
		)
		ConnectionState.Online  -> EclosetScreen(
			connectionViewModel = connectionViewModel
		)
	}
}

@Composable
fun EclosetScreen(
	connectionViewModel: ConnectionViewModel
){
	val userViewModel: UserViewModel = viewModel()
	val navController = rememberNavController()
	val backStackEntry by navController.currentBackStackEntryAsState()
	val currentRoute = backStackEntry?.destination?.route
	val loggedIn = userViewModel.userState.collectAsState().value.loggedIn
	val showNavBar = (currentRoute != Routes.LOGIN && currentRoute != Routes.REGISTER &&
			currentRoute != Routes.USER_SETTINGS && currentRoute != Routes.ABOUT)

	val snackbarHostState = remember { SnackbarHostState() }

	Scaffold(
		modifier = Modifier.fillMaxSize(),
		topBar = { Header(
			navController = navController,
			userViewModel = userViewModel
		) },
		snackbarHost = {
			SnackbarHost(snackbarHostState)
		},
		floatingActionButton = {
			if (showNavBar && loggedIn) {
				ActionBarMenu(
					navController = navController,
					currentRoute = currentRoute
				)
			}},
		floatingActionButtonPosition = FabPosition.Center
	) {
		innerPadding ->
		NavActionHost(
			modifier = Modifier
				.fillMaxSize()
				.padding(top = 45.dp),
			navController = navController,
			innerPadding = innerPadding,
			userViewModel = userViewModel,
			snackbarHostState = snackbarHostState
		)
	}
}
