package com.example.ecloset.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ecloset.user.local.ClosetItemData

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
}
