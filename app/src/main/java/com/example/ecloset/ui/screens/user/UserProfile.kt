package com.example.ecloset.ui.screens.user

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.ecloset.user.local.viewmodels.UserViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.ecloset.ui.components.PageHeader
import com.example.ecloset.ui.components.RadioGroupWithDropdown
import com.example.ecloset.user.data.Presets.locations
import com.example.ecloset.user.data.Presets.themePresets
import com.example.ecloset.user.data.Presets.selectOptions

@Composable
fun UserProfile(
	modifier: Modifier = Modifier,
	navController: NavHostController,
	userViewModel: UserViewModel
){
	val state by userViewModel.userState.collectAsStateWithLifecycle()
	val user = state.user

	Box(
		modifier = modifier
			.fillMaxSize()
	) {
		LazyColumn(
			modifier
				.fillMaxWidth(),
			contentPadding = PaddingValues(top = 0.dp)
		) {
			item {
				PageHeader(
					title = stringResource(R.string.user_profile_title),
					navController = navController
				)
			}
			item {
				val avatarUrl = user?.avatarUrl
				var pendingUri by remember { mutableStateOf<Uri?>(null) }
				val photoPicker = rememberLauncherForActivityResult(
					contract = ActivityResultContracts.PickVisualMedia()
				) { uri: Uri? ->
					if (uri != null) {
						pendingUri = uri
						userViewModel.updateAvatarUrl(uri.toString())
					}
				}

				Column(
					modifier = Modifier
						.fillMaxWidth(),
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					val previewModel = pendingUri?.toString() ?: avatarUrl

					if (avatarUrl == null) {
						Icon(
							imageVector = Icons.Default.AccountCircle,
							contentDescription = "profile picture",
							modifier = Modifier
								.width(150.dp)
								.aspectRatio(1f),
							tint = MaterialTheme.colorScheme.tertiary
						)
					} else {
						AsyncImage(
							model = previewModel,
							contentDescription = "profile picture",
							contentScale = ContentScale.Crop,
							modifier = Modifier
								.width(150.dp)
								.aspectRatio(1f)
								.clip(CircleShape)
						)
					}

					Button(
						modifier = Modifier
							.width(300.dp)
							.padding(
								horizontal = 16.dp,
								vertical = 8.dp
							),
						onClick = {
							photoPicker.launch(
								PickVisualMediaRequest(
									ActivityResultContracts.PickVisualMedia.ImageOnly
								)
							)
						}
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
			item {
				EditableLabel(
					title = stringResource(R.string.username_title),
					value = userViewModel.userState.collectAsState().value.user?.username
						?: String(),
					onClick = { value ->
						userViewModel.updateUsername(value)
					}
				)

				EditableLabel(
					title = stringResource(R.string.email_title),
					value = userViewModel.userState.collectAsState().value.user?.email ?: String(),
					onClick = { value ->
						userViewModel.updateEmail(value)
					}
				)
			}
			item {
				val locationDynamic = user?.locationDynamic ?: false
				val location = user?.location?.location ?: ""
				val locationStrings = locations.map { it.location }

				GroupTitle(
					title = stringResource(R.string.location_title),
					caption = location,
					showCaption = true
				)

				RadioGroupWithDropdown(
					options = selectOptions,
					items = locationStrings,
					selectedIndex = if (locationDynamic) 1 else 0,
					onOptionSelected = { index ->
						userViewModel.updateLocationDynamic(index == 1)
					},
					selectedItem = themePresets.indexOf(location),
					onItemSelected = { index ->
						val loc = locationStrings[index]
						val newLocation = locations.find {
							it.location == loc
						}
						newLocation?.let { userViewModel.updateLocation(it) }
					},
					dropIndex = 0
				)

			}
			item {
				val themeDynamic = user?.themeDynamic ?: false
				val themePreset = user?.themePreset ?: themePresets.first()

				GroupTitle(
					title = stringResource(R.string.theme_title)
				)

				RadioGroupWithDropdown(
					options = selectOptions,
					items = themePresets,
					selectedIndex = if (themeDynamic) 1 else 0,
					onOptionSelected = { index ->
						userViewModel.updateThemeDynamic(index == 1)
					},
					selectedItem = themePresets.indexOf(themePreset),
					onItemSelected = { index ->
						userViewModel.updateThemePreset(themePresets[index])
					},
					dropIndex = 0
				)
			}
			item {
				Spacer(modifier = Modifier.padding(vertical = 8.dp))
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
			item {
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
			item {
				Spacer(modifier = Modifier.padding(vertical = 32.dp))
			}
		}
	}
}

@Composable
fun EditableLabel(
	title: String,
	value: String,
	onClick: (String) -> Unit
){
	var newValue = value
	var expanded by remember { mutableStateOf(false) }

	Text(
		text = title,
		modifier = Modifier
			.fillMaxWidth()
			.padding(
				horizontal = 16.dp,
				vertical = 8.dp
			)
			.clickable{
				expanded = !expanded
			},
		style = MaterialTheme.typography.bodyLarge
	)
	if (!expanded) {
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
	} else {
		OutlinedTextField(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 8.dp, vertical = 8.dp),
			value = value,
			onValueChange = { newValue = it },
			shape = RoundedCornerShape(16.dp),
			singleLine = true,
			placeholder = {
				Text(stringResource(R.string.new_value_prompt))
			}
		)

		Button(
			modifier = Modifier
				.fillMaxWidth()
				.padding(
					horizontal = 90.dp,
					vertical = 8.dp
				),
			onClick = {
				expanded = !expanded
				onClick(newValue)
			}
		) {
			Text(
				text = stringResource(R.string.save_prompt),
				modifier = Modifier
					.fillMaxWidth()
					.padding(
						horizontal = 12.dp,
						vertical = 8.dp
					),
				style = MaterialTheme.typography.bodyMedium,
				color = MaterialTheme.colorScheme.onPrimary,
				textAlign = TextAlign.Center
			)
		}
	}

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
