package com.example.ecloset.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ecloset.R
import androidx.compose.ui.graphics.Color

@Composable
fun ActionBarButton(
	isSelected: Boolean,
	iconImage: ImageVector,
	description: String,
	primaryColor: Color,
	secondaryColor: Color,
	onClick: () -> Unit
) {
	FloatingActionButton(
		onClick = onClick,
		containerColor = if (isSelected)
			primaryColor
		else
			secondaryColor,
		modifier = Modifier.size(56.dp)
	) {
		Icon(
			imageVector = iconImage,
			contentDescription = description,
			tint = if (isSelected)
				secondaryColor
			else
				primaryColor
		)
	}
}

@Composable
fun ActionBarMenu(){
	var selectedPage by remember { mutableStateOf(0) }
	var addMenuOpen by remember { mutableStateOf(false) }

	// bottom aligned fab row
	Box(
		modifier = Modifier.fillMaxSize(),
		contentAlignment = Alignment.BottomCenter
	) {
		Row(
			horizontalArrangement = Arrangement.Center,
			modifier = Modifier.padding(bottom = 16.dp)
		) {
			ActionBarButton(
				isSelected = selectedPage == 0,
				iconImage = Icons.Default.Home,
				description = stringResource(R.string.mainpage_title),
				primaryColor = MaterialTheme.colorScheme.primary,
				secondaryColor = MaterialTheme.colorScheme.secondaryContainer,
				onClick = { selectedPage = 0 }
			)
			Spacer(modifier = Modifier.padding(horizontal = 8.dp))
			ActionBarButton(
				isSelected = addMenuOpen,
				iconImage = Icons.Default.AddCircle,
				description = stringResource(R.string.add_prompt) + " " +
							stringResource(R.string.item_text) + " or " +
							stringResource(R.string.outfit_text),
				primaryColor = MaterialTheme.colorScheme.onTertiary,
				secondaryColor = MaterialTheme.colorScheme.tertiary,
				onClick = { addMenuOpen = !addMenuOpen }
			)
			Spacer(modifier = Modifier.padding(horizontal = 8.dp))
			ActionBarButton(
				isSelected = selectedPage == 1,
				iconImage = Icons.Default.Book,
				description = stringResource(R.string.analytics_title),
				primaryColor = MaterialTheme.colorScheme.primary,
				secondaryColor = MaterialTheme.colorScheme.secondaryContainer,
				onClick = { selectedPage = 1 }
			)
			Spacer(modifier = Modifier.padding(horizontal = 8.dp))
		}
	}
}

@Composable
fun ActionBarMenuPrototype(){
	var selectedPage by remember { mutableStateOf(0) }
	Box(
		modifier = Modifier.fillMaxSize(),
		contentAlignment = Alignment.BottomCenter
	) {
		Row(
			horizontalArrangement = Arrangement.Center,
			modifier = Modifier.padding(bottom = 16.dp)
		) {
			// Page 1
			FloatingActionButton(
				onClick = { selectedPage = 0 },
				containerColor = if (selectedPage == 0)
					MaterialTheme.colorScheme.primary
				else
					MaterialTheme.colorScheme.secondaryContainer,
				modifier = Modifier.size(56.dp)
			) {
				Icon(
					imageVector = Icons.Default.Home,
					contentDescription = stringResource(R.string.mainpage_title),
					tint = if (selectedPage == 0)
						MaterialTheme.colorScheme.onPrimary
					else
						MaterialTheme.colorScheme.onSecondaryContainer
				)
			}
			Spacer(modifier = Modifier.padding(horizontal = 8.dp))

			// Add
			FloatingActionButton(
				onClick = {
					// add outfit/item
				},
				containerColor = MaterialTheme.colorScheme.tertiary,
				modifier = Modifier.size(56.dp)
			) {
				Icon(
					imageVector = Icons.Default.Add,
					contentDescription = stringResource(R.string.add_prompt) + " " +
							stringResource(R.string.item_text) + " or " +
							stringResource(R.string.outfit_text),
					tint = MaterialTheme.colorScheme.onTertiary,
					modifier = Modifier.size(32.dp)
				)
			}
			Spacer(modifier = Modifier.padding(horizontal = 8.dp))

			// Page 2
			FloatingActionButton(
				onClick = { selectedPage = 1 },
				containerColor = if (selectedPage == 1)
					MaterialTheme.colorScheme.primary
				else
					MaterialTheme.colorScheme.secondaryContainer,
				modifier = Modifier.size(56.dp)
			) {
				Icon(
					imageVector = Icons.Default.Book,
					contentDescription = stringResource(R.string.analytics_title),
					tint = if (selectedPage == 1)
						MaterialTheme.colorScheme.onPrimary
					else
						MaterialTheme.colorScheme.onSecondaryContainer
				)
			}
		}
	}
}