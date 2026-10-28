package com.example.ecloset.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ecloset.R
import com.example.ecloset.user.local.ClosetItemData
import com.example.ecloset.user.local.OutfitItemData

@Composable
fun OutfitCollageCanvas(
	selectedItems: List<ClosetItemData>
){
	val maxSize = 250.dp

	Canvas(
		modifier = Modifier
			.sizeIn(
				maxWidth = maxSize,
				maxHeight = maxSize
			)
			.aspectRatio(1f)
	){}

	Box(
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp)
	){
		selectedItems.forEach { item ->
			// preview in category tab
		}
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
			text = stringResource(com.example.ecloset.R.string.add_prompt) +
					" " +
					stringResource(R.string.outfit_text),
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
