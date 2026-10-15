@file:OptIn(ExperimentalMaterial3Api::class)
@file:SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")

/* to-do:
	main:
		user actions (change username, etc)
		closet viewmodels
		connect backend
		user tint selecting block to user settings
		filters for outfits/clothing (with some presets: category for items, year/occasion for outfits)
		weather widget (add cute animation/gif)
		fix up preview card classes
	medium:
		user theme preference + dynamic tinting for theme
		add localizations to clothes categories
		add statistics for clothes categories
		add unused clothes
		preview cards (last added items, relevant outfits)
	misc:
		fix up log/reg pages (cutify)
		remove type attribute from data classes, replace with type check
*/

package com.example.ecloset

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.ecloset.ui.theme.EClosetTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ecloset.ui.components.ActionBarMenu
import com.example.ecloset.ui.components.Header
import androidx.navigation.compose.rememberNavController
import com.example.ecloset.ui.navigation.NavActionHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.ecloset.ui.navigation.Routes
import com.example.ecloset.user.data.UserViewModel

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		window.isNavigationBarContrastEnforced = false
		setContent {
			EClosetTheme {
				EclosetScreen()
			}
		}
	}
}

@Composable
fun EclosetScreen(){
	val userViewModel: UserViewModel = viewModel()
	val navController = rememberNavController()
	val backStackEntry by navController.currentBackStackEntryAsState()
	val currentRoute = backStackEntry?.destination?.route
	val showNavBar = (currentRoute != Routes.LOGIN && currentRoute != Routes.REGISTER &&
			currentRoute != Routes.USER_SETTINGS && currentRoute != Routes.ABOUT)

	Scaffold(
		modifier = Modifier.fillMaxSize(),
		topBar = { Header(
			navController = navController,
			userViewModel = userViewModel
		) },
		floatingActionButton = {
			if (showNavBar) {
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
			userViewModel = userViewModel
		)
	}
}
