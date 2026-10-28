package com.example.ecloset.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.ecloset.R
import com.example.ecloset.user.data.Presets.clothesCategories
import com.example.ecloset.user.local.CategoryItem
import com.example.ecloset.user.local.ClosetItemData
import com.example.ecloset.user.local.OutfitItemData
import com.example.ecloset.user.local.PreviewCardData
import com.example.ecloset.user.data.Presets.colorList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewGridCard(
	data: PreviewCardData
){
	var showDetails by remember { mutableStateOf(false) }

	Card(
		onClick = {
			showDetails = true
		},
		modifier = Modifier
			.width(150.dp)
			.height(200.dp),
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.surfaceVariant,
			contentColor = MaterialTheme.colorScheme.onSurfaceVariant
		),
		elevation = CardDefaults.cardElevation(
			defaultElevation = 2.dp
		)
	) {
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.padding(8.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			val previewModel: Any? = when (data) {
				is OutfitItemData -> {
					data.imageUrl ?: R.drawable.thimking
				}
				is ClosetItemData -> {
					data.imageUrl ?: R.drawable.hanger
				}
				else -> { null }
			}

			val tintVar: Boolean = when (data) {
				is OutfitItemData -> {
					false
				}
				is ClosetItemData -> {
					data.imageUrl == null
				}
				else -> { true }
			}

			AsyncImage(
				model = previewModel,
				contentDescription = "preview image",
				contentScale = ContentScale.Fit,
				colorFilter = if (tintVar) {
					ColorFilter.tint(MaterialTheme.colorScheme.tertiary)
				} else {
					null
				},
				modifier = Modifier
					.height(130.dp)
					.padding(16.dp)
			)
			Text(
				text = data.title,
				style = MaterialTheme.typography.bodySmall,
				color = MaterialTheme.colorScheme.tertiary,
				textAlign = TextAlign.Center,
				maxLines = 2
			)
		}
	}

	if (showDetails){
		DetailsPopup(
			onDismiss = {
				showDetails = false
			},
			content = {
				CardDetails(
					data = data
				)
			}
		)
	}
}

@Composable
fun CardDetails(
	data: PreviewCardData
){
	LazyColumn(
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		item{
			Text(
				text = data.title,
				style = MaterialTheme.typography.titleLarge
			)
		}
		item{
			val type: String? = when (data) {
				is OutfitItemData -> {
					stringResource(R.string.outfit_text)
				}
				is ClosetItemData -> {
					stringResource(R.string.item_text)
				}
				else -> { "" }
			}
			Text(
				text = type + ", " + data.dateAdded.toString(),
				style = MaterialTheme.typography.bodyMedium
			)
		}
		item{
			val previewModel: Any? = when (data) {
				is OutfitItemData -> {
					data.imageUrl ?: R.drawable.thimking
				}
				is ClosetItemData -> {
					data.imageUrl ?: R.drawable.hanger
				}
				else -> { null }
			}

			AsyncImage(
				model = previewModel,
				contentDescription = "preview image",
				contentScale = ContentScale.Fit,
				modifier = Modifier
					.height(130.dp)
					.padding(16.dp)
			)
		}
		item{
			// find color by id or title
			when (data) {
				is OutfitItemData -> {
					Text(
						text = data.season,
						style = MaterialTheme.typography.bodyMedium
					)
					Text(
						text = data.occasion,
						style = MaterialTheme.typography.bodyMedium
					)
					LabelText(
						text = colorList[data.color - 1].name,
						color = colorList[data.color - 1].value
					)
				}
				is ClosetItemData -> {
					Text(
						text = data.season,
						style = MaterialTheme.typography.bodyMedium
					)
					Text(
						text = stringResource(clothesCategories[data.category].titleId),
						style = MaterialTheme.typography.bodyMedium
					)
					Text(
						text = data.occasion,
						style = MaterialTheme.typography.bodyMedium
					)
					LabelText(
						text = colorList[data.color - 1].name,
						color = colorList[data.color - 1].value
					)
					Text(
						text = data.material,
						style = MaterialTheme.typography.bodyMedium
					)
				}
			}
		}
	}
}

@Composable
fun CategoryCard(
	category: CategoryItem,
	color: Color = MaterialTheme.colorScheme.onSecondaryContainer,
	backgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer
){
	Card(
		modifier = Modifier
			//.aspectRatio(1f)
			.height(50.dp),
		colors = CardDefaults.cardColors(
			containerColor = backgroundColor
		),
		elevation = CardDefaults.cardElevation(
			defaultElevation = 2.dp
		)
		//shape = RoundedCornerShape(percent = 50)
	) {
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.padding(12.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Text(
				text = stringResource(category.titleId),
				style = MaterialTheme.typography.titleMedium,
				color = color,
				maxLines = 1
			)
		}
	}
}

@Composable
fun EmptyPreviewCard(){
	Card(
		onClick = {},
		modifier = Modifier
			.width(150.dp)
			.height(75.dp),
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.surfaceVariant,
			contentColor = MaterialTheme.colorScheme.onSurfaceVariant
		),
		elevation = CardDefaults.cardElevation(
			defaultElevation = 2.dp
		)
	){
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.fillMaxHeight()
				.padding(8.dp),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center
		) {
			Text(
				text = stringResource(R.string.no_content),
				style = MaterialTheme.typography.titleSmall,
				color = MaterialTheme.colorScheme.tertiary,
				maxLines = 1
			)
		}
	}
}

@Composable
fun LabelText(
	modifier: Modifier = Modifier,
	text: String,
	icon: ImageVector = Icons.Default.Circle,
	color: Color = MaterialTheme.colorScheme.primary,
	style: TextStyle =  MaterialTheme.typography.bodyMedium
){
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(4.dp)
	) {
		Icon(
			imageVector = icon,
			contentDescription = null,
			modifier = modifier
				.size(16.dp),
			tint = color
		)
		Text(
			text = text,
			style = style
		)
	}
}
