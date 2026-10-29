package com.example.ecloset.ui.screens.user

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.ecloset.BuildConfig
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.ecloset.R
import androidx.navigation.NavHostController
import com.example.ecloset.ui.components.PageHeader

@Composable
fun AboutPage(
	modifier: Modifier = Modifier,
	navController: NavHostController
){
	val context = LocalContext.current

	Box(
		modifier = modifier
			.fillMaxSize()
	) {
		LazyColumn(
			modifier = modifier
				.fillMaxWidth(),
			contentPadding = PaddingValues(top = 0.dp)
		) {
			item {
				PageHeader(
					title = stringResource(R.string.about_title),
					navController = navController
				)
			}
			item{
				Image(
					painter = painterResource(R.drawable.pigeon),
					contentDescription = null,
					contentScale = ContentScale.FillBounds,
					modifier = Modifier
						.fillMaxWidth()
						.padding(
							horizontal = 40.dp,
							vertical = 8.dp
						)
						.clip(RoundedCornerShape(20.dp))
				)
			}
			item {
				Text(
					text = stringResource(R.string.about_description),
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.onSurfaceVariant,
					textAlign = TextAlign.Center,
					modifier = Modifier
						.fillMaxWidth()
						.padding(horizontal = 16.dp)
				)
				Spacer(modifier = Modifier.padding(8.dp))
			}
			item {
				ListItem(
					modifier = Modifier
						.padding(top = 8.dp)
						.fillMaxWidth(),
					headlineContent = { Text(stringResource(R.string.about_devs)) },
					leadingContent = {
						Icon(
							imageVector = Icons.Filled.Contacts,
							contentDescription = null
						)
					},
					supportingContent = {
						Column(
							modifier = Modifier
								.fillMaxWidth(),
							verticalArrangement = Arrangement.spacedBy(0.dp)
						) {
							Text(
								text = stringResource(R.string.about_devs_1),
								modifier = Modifier
									.clickable {
										context.startActivity(
											Intent(
												Intent.ACTION_VIEW,
												BuildConfig.DEV_URL_1.toUri()
											)
										)
									}
							)
							Text(
								text = stringResource(R.string.about_devs_2),
								modifier = Modifier
									.clickable {
										context.startActivity(
											Intent(
												Intent.ACTION_VIEW,
												BuildConfig.DEV_URL_2.toUri()
											)
										)
									}
							)
						}
					},
					colors = ListItemDefaults.colors(
						containerColor = MaterialTheme.colorScheme.background
					)
				)
			}
			item {
				ListItem(
					headlineContent = {
						Text(stringResource(R.string.about_github))
					},
					supportingContent = {
						Text("github.com/yeeltheeel/ecloset")
					},
					leadingContent = {
						Icon(
							imageVector = Icons.Filled.Code,
							contentDescription = null
						)
					},
					colors = ListItemDefaults.colors(
						containerColor = MaterialTheme.colorScheme.background
					),
					modifier = Modifier
						.fillMaxWidth()
						.clickable {
							val intent = Intent(
								Intent.ACTION_VIEW,
								"https://github.com/yeeltheeel/ecloset".toUri()
							)
							context.startActivity(intent)
						}
				)
			}
			item {
				ListItem(
					headlineContent = {
						Text(stringResource(R.string.about_version))
					},
					supportingContent = {
						val appVersion = BuildConfig.VERSION_NAME
						Text(appVersion)
					},
					leadingContent = {
						Icon(
							imageVector = Icons.Filled.Info,
							contentDescription = null
						)
					},
					colors = ListItemDefaults.colors(
						containerColor = MaterialTheme.colorScheme.background
					),
				)
			}
		}
	}
}