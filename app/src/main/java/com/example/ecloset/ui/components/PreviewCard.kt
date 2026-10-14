package com.example.ecloset.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ecloset.user.data.CategoryItem
import com.example.ecloset.user.data.PreviewCard
import com.example.ecloset.ui.components.DetailsPopup

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewGridCard(
	data: PreviewCard
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
		),
	) {
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.padding(8.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Spacer(
				// Temporary image placeholder
				modifier = Modifier
					.size(130.dp)
					.padding(bottom = 8.dp)
			)
			Text(
				text = data.title,
				style = MaterialTheme.typography.titleMedium,
				color = MaterialTheme.colorScheme.tertiary,
				maxLines = 1
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
					data = data,
					type = data.type
				)
			}
		)
	}
}

@Composable
fun CardDetails(
	data: PreviewCard,
	type: String
){
	LazyColumn(
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp)
	) {
		item{
			Text(
				text = data.title,
				style = MaterialTheme.typography.titleMedium
			)
		}
		item{
			Text(
				text = type + " added: " + data.dateAdded.toString(),
				style = MaterialTheme.typography.bodyMedium
			)
		}
	}
}

@Composable
fun CategoryCard(
	category: CategoryItem
){
	Card(
		modifier = Modifier
			//.aspectRatio(1f)
			.height(50.dp),
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.secondaryContainer
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
				color = MaterialTheme.colorScheme.onSecondaryContainer,
				maxLines = 1
			)
		}
	}
}
