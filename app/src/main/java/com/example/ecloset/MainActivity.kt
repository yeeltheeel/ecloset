@file:OptIn(ExperimentalMaterial3Api::class)
@file:SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")

/* todo:
	main:
		functions:
			connect backend in full:
				dto
				tokens
				enums
				api
			call weather from backend
			implement user theme preference (light/dark/system, static/dynamic)
		widgets:
			after item image loaded show data and add button
			outfit collage
			new outfit quiz is a popup
			location select
			filters block and page
			item/oufit details clean up and add actions
			add user tint selecting block to user settings
			outfit preview card grays out if item not available
		general:
			image cache for outfit/item preview
			last added items, relevant outfits
			add load more button to grid
			login if email then otp input else email first
			resend code button with timer to otp page
			filters for outfits/clothing (with some presets: category for items, year/occasion for outfits)
	medium:
		add localizations for all hardcoded strings
		prettify weather widget with lottie
	misc:
		add admin/support functions
*/

package com.example.ecloset

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner

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
	val connectionViewModel: ConnectionViewModel = hiltViewModel()
	val connectionState by connectionViewModel.state.collectAsState()
	//Log.d("AppDebug", "connectionState = $connectionState")

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
	//val userViewModel: UserViewModel = hiltViewModel()
	val userViewModel: UserViewModel = androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel(
		checkNotNull<androidx.lifecycle.ViewModelStoreOwner>(LocalViewModelStoreOwner.current) {
			"No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
		},
		null
	)
	val snackbarHostState = remember { SnackbarHostState() }
	val navController = rememberNavController()
	val backStackEntry by navController.currentBackStackEntryAsState()
	val currentRoute = backStackEntry?.destination?.route
	val loggedIn = userViewModel.userState.collectAsState().value.loggedIn
	val showNavBar = (currentRoute != Routes.LOGIN && currentRoute != Routes.REGISTER &&
			currentRoute != Routes.USER_SETTINGS && currentRoute != Routes.ABOUT)

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
