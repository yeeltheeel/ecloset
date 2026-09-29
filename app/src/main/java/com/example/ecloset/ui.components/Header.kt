package com.example.ecloset.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.ecloset.R
import com.example.ecloset.ui.components.PopupActionMenu
import com.example.ecloset.ui.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(
	navController: NavHostController
){
	val userAvatarUrl: String? = null

	var userMenuOpen by remember { mutableStateOf(false) }
	val userMenuActions = listOf(
		PopupAction(
			content = stringResource(R.string.user_profile_title),
			navRoute = Routes.USER_SETTINGS,
		),
		PopupAction(
			content = stringResource(R.string.about_title),
			navRoute = Routes.ABOUT,
		)
	)

	TopAppBar(
		title = {Text(text = stringResource(R.string.app_name))},
		actions = {
			Box{
				IconButton(
					onClick = {
						// Open and close the menu
					},
				) {
					if (userAvatarUrl == null) {
						// default avatar when no image is available.
						Icon(
							imageVector = Icons.Default.AccountCircle,
							contentDescription = "User profile",
							tint = MaterialTheme.colorScheme.onPrimary
						)
					} else {
						// display the user's image here later.
						Icon(
							imageVector = Icons.Default.AccountCircle,
							contentDescription = "User profile",
							tint = MaterialTheme.colorScheme.onPrimary
						)
					}
				}
				PopupActionMenu(
					modifier = Modifier,
					expanded = userMenuOpen,
					direction = PopupDirection.DOWN,
					actions = userMenuActions,
					navController = navController,
					onDismiss = {
						userMenuOpen = false
					}
				)
			}
		},
		colors = TopAppBarDefaults.topAppBarColors(
			containerColor = MaterialTheme.colorScheme.primary,
			titleContentColor = MaterialTheme.colorScheme.onPrimary,
			actionIconContentColor = MaterialTheme.colorScheme.onPrimary
		)
	)
}