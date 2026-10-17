package com.example.ecloset.ui.screens.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ecloset.R
import com.example.ecloset.ui.navigation.Routes
import com.example.ecloset.user.data.UserViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ecloset.ui.components.PageHeader
import com.example.ecloset.ui.components.RadioGroupBlock
import com.example.ecloset.ui.components.RadioGroupWithDropdown
import com.example.ecloset.ui.theme.Tint.Companion.Amber
import com.example.ecloset.ui.theme.Tint.Companion.Indigo
import com.example.ecloset.ui.theme.Tint.Companion.Rose
import com.example.ecloset.ui.theme.Tint.Companion.Sage
import com.example.ecloset.ui.theme.Tint.Companion.Violet
import com.example.ecloset.user.data.locations
import com.example.ecloset.user.data.presets
import com.example.ecloset.user.data.selectOptions

@Composable
fun UserProfile(
	modifier: Modifier = Modifier,
	navController: NavHostController,
	userViewModel: UserViewModel
){
	val state by userViewModel.userState.collectAsStateWithLifecycle()
	val user = state.user

	LazyColumn(
		modifier
			.fillMaxWidth(),
		contentPadding = PaddingValues(top = 0.dp)
	) {
		item{
			PageHeader(
				title = stringResource(R.string.user_profile_title),
				navController = navController
			)
		}
		item{
			val avatarUrl = userViewModel.userState.collectAsState().value.user?.avatarUrl

			Column(
				modifier = Modifier
					.fillMaxWidth(),
				horizontalAlignment = Alignment.CenterHorizontally
			){
				if (avatarUrl == null){
					Icon(
						imageVector = Icons.Default.AccountCircle,
						contentDescription = "profile picture",
						modifier = Modifier
							.width(150.dp)
							.aspectRatio(1f),
						tint = MaterialTheme.colorScheme.tertiary
					)
				}

				Button(
					modifier = Modifier
						.width(300.dp)
						.padding(
							horizontal = 16.dp,
							vertical = 8.dp
						),
					onClick = {}
				) {
					Text(
						text = stringResource(com.example.ecloset.R.string.change_avatar_prompt),
						modifier = Modifier
							.fillMaxWidth()
							.padding(
								horizontal = 12.dp,
								vertical = 8.dp
							),
						style = MaterialTheme.typography.bodyMedium,
						color = MaterialTheme.colorScheme.onPrimary,
						textAlign = TextAlign.Center,
						maxLines = 1
					)
				}
			}

			Spacer(modifier = Modifier.padding(vertical = 8.dp))
		}
		item{
			EditableLabel(
				title = stringResource(R.string.username_title),
				value = userViewModel.userState.collectAsState().value.user?.username ?: String(),
				onClick = {}
			)

			EditableLabel(
				title = stringResource(R.string.email_title),
				value = userViewModel.userState.collectAsState().value.user?.email ?: String(),
				onClick = {}
			)
		}
		item {
			val locationDynamic = user?.locationDynamic ?: false
			val location = user?.location ?: ""

			GroupTitle(
				title = stringResource(R.string.location_title),
				caption = location,
				showCaption = true
			)

			RadioGroupWithDropdown(
				options = selectOptions,
				items = locations,
				selectedIndex = if (locationDynamic) 1 else 0,
				onOptionSelected = { index ->
					userViewModel.updateLocationDynamic(index == 1)
				},
				selectedItem = presets.indexOf(location),
				onItemSelected = {index ->
					userViewModel.updateLocation(locations[index])
				},
				dropIndex = 0
			)

		}
		item {
			val themeDynamic = user?.themeDynamic ?: false
			val themePreset  = user?.themePreset ?: presets.first()

			GroupTitle(
				title = stringResource(R.string.theme_title)
			)

			RadioGroupWithDropdown(
				options = selectOptions,
				items = presets,
				selectedIndex = if (themeDynamic) 1 else 0,
				onOptionSelected = { index ->
					userViewModel.updateThemeDynamic(index == 1)
				},
				selectedItem = presets.indexOf(themePreset),
				onItemSelected = {index ->
					userViewModel.updateThemePreset(presets[index])
				},
				dropIndex = 0
			)
		}
		item{
			Spacer(modifier = Modifier.padding(vertical = 16.dp))
			Button(
				modifier = Modifier
					.fillMaxWidth()
					.padding(
						horizontal = 50.dp,
						vertical = 8.dp
					),
				onClick = {
					userViewModel.logOut()
				}
			) {
				Text(
					text = stringResource(com.example.ecloset.R.string.logout_promt),
					modifier = Modifier
						.fillMaxWidth()
						.padding(
							horizontal = 16.dp,
							vertical = 12.dp
						),
					style = MaterialTheme.typography.titleLarge,
					color = MaterialTheme.colorScheme.onPrimary,
					textAlign = TextAlign.Center
				)
			}
		}
		item{
			Button(
				modifier = Modifier
					.fillMaxWidth()
					.padding(
						horizontal = 50.dp,
						vertical = 4.dp
					),
				onClick = {
					userViewModel.deleteAccount()
				},
				colors = ButtonDefaults.buttonColors(
					containerColor = MaterialTheme.colorScheme.errorContainer
				)
			) {
				Text(
					text = stringResource(com.example.ecloset.R.string.delete_account_prompt),
					modifier = Modifier
						.fillMaxWidth()
						.padding(
							horizontal = 16.dp,
							vertical = 12.dp
						),
					style = MaterialTheme.typography.titleLarge,
					color = MaterialTheme.colorScheme.onErrorContainer,
					textAlign = TextAlign.Center
				)
			}
		}
	}
}

@Composable
fun EditableLabel(
	title: String,
	value: String,
	onClick: () -> Unit
){
	Text(
		text = title,
		modifier = Modifier
			.fillMaxWidth()
			.padding(
				horizontal = 16.dp,
				vertical = 8.dp
			)
			.clickable{
				onClick()
			},
		style = MaterialTheme.typography.bodyLarge
	)
	Text(
		text = value,
		modifier = Modifier
			.fillMaxWidth()
			.padding(
				horizontal = 16.dp,
				vertical = 0.dp
			),
		style = MaterialTheme.typography.bodyMedium,
		color = MaterialTheme.colorScheme.tertiary
	)
	Spacer(modifier = Modifier.padding(vertical = 8.dp))
}

@Composable
fun GroupTitle(
	title: String,
	caption: String = "",
	showCaption: Boolean = false
){
	Spacer(modifier = Modifier.padding(vertical = 8.dp))
	Text(
		text = title,
		modifier = Modifier
			.fillMaxWidth()
			.padding(
				horizontal = 16.dp,
				vertical = 8.dp
			),
		style = MaterialTheme.typography.titleMedium
	)
	if (showCaption){
		Text(
			text = caption,
			modifier = Modifier
				.fillMaxWidth()
				.padding(
					horizontal = 16.dp,
					vertical = 0.dp
				),
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.tertiary
		)
		Spacer(modifier = Modifier.padding(vertical = 8.dp))
	}
}

@Composable
fun RadioDropdown(
	choices: List<String>,
	presets: List<String>,
	userViewModel: UserViewModel
){
	val choice = true

	RadioButton(
		selected = true,
		onClick = {}
	)
	RadioButton(
		selected = false,
		onClick = {}
	)
}
