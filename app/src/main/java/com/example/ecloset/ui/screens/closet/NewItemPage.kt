package com.example.ecloset.ui.screens.closet

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.ecloset.R
import com.example.ecloset.ui.components.EditableLabel
import com.example.ecloset.ui.components.PageHeader
import com.example.ecloset.user.local.viewmodels.NewItemViewmodel

@Composable
fun NewItemPage(
	modifier: Modifier = Modifier,
	navController: NavHostController,
	newItemViewmodel: NewItemViewmodel = hiltViewModel()
){
	val newItem by newItemViewmodel.newItem.collectAsStateWithLifecycle()
	var loadedClicked by remember { mutableStateOf(false) }
	var infoLoaded by remember { mutableStateOf(false) }

	Box(
		modifier = modifier
			.fillMaxSize()
	) {
		LazyColumn(
			modifier
				.fillMaxWidth(),
			contentPadding = PaddingValues(top = 0.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			item {
				PageHeader(
					title = stringResource(R.string.new_item_title),
					navController = navController
				)
			}
			item {
				var pendingUri by remember { mutableStateOf<Uri?>(null) }
				val photoPicker = rememberLauncherForActivityResult(
					contract = ActivityResultContracts.PickVisualMedia()
				) { uri: Uri? ->
					if (uri != null) {
						pendingUri = uri
					}
				}
				val previewModel = pendingUri?.toString() ?: R.drawable.hanger

				Box(
					modifier = Modifier
						.size(250.dp)
						.clip(RoundedCornerShape(16.dp))
						.background(MaterialTheme.colorScheme.surfaceVariant),
					contentAlignment = Alignment.Center,
				){
					AsyncImage(
						model = previewModel,
						contentDescription = null,
						contentScale = ContentScale.Fit,
						colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.tertiary),
						modifier = Modifier
							.width(150.dp)
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
						// send image to server
						loadedClicked = true
					}
				) {
					Text(
						text = stringResource(com.example.ecloset.R.string.load_image_prompt),
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
			item {
				// process server response
				//infoLoaded = true
				loadedClicked = false
			}
			item {
				if (infoLoaded) {
					// server response data goes here
					EditableLabel(
						title = stringResource(R.string.item_title_text),
						value = "",
						style = MaterialTheme.typography.titleMedium,
						onClick = {
							// newItem.updateTitle()
						}
					)
					// some should be editable dropdowns
					EditableLabel(
						title = stringResource(R.string.item_color_text),
						value = "",
						style = MaterialTheme.typography.titleMedium,
						onClick = {
							// newItem.updateColor()
						}
					)
					EditableLabel(
						title = stringResource(R.string.item_season_text),
						value = "",
						style = MaterialTheme.typography.titleMedium,
						onClick = {
							// newItem.updateSeason()
						}
					)
					EditableLabel(
						title = stringResource(R.string.item_material_text),
						value = "",
						style = MaterialTheme.typography.titleMedium,
						onClick = {
							// newItem.updateSeason()
						}
					)
				}
			}
			item {
				if (infoLoaded){
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
							text = stringResource(com.example.ecloset.R.string.add_prompt) +
									" " +
									stringResource(R.string.item_text),
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
			}
		}
	}
}