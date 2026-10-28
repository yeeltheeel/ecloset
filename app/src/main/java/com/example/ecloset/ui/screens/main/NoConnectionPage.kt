package com.example.ecloset.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ecloset.R

@Composable
fun NoConnectionPage(
	modifier: Modifier = Modifier,
	onRetry: () -> Unit
){
	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background),
		contentAlignment = Alignment.Center
	) {
		Column(
			modifier = Modifier
				.padding(
					vertical = 20.dp,
					horizontal = 16.dp
				)
				.fillMaxHeight()
				.fillMaxWidth(),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			Image(
				painter = painterResource(R.drawable.noconenction),
				contentDescription = null,
				contentScale = ContentScale.FillWidth,
				modifier = Modifier
					.fillMaxWidth()
					.padding(
						vertical = 8.dp,
						horizontal = 16.dp
					)
					.clip(RoundedCornerShape(20.dp))
			)
			Text(
				modifier = Modifier
					.padding(
						vertical = 8.dp
					)
					.fillMaxWidth(),
				text = stringResource(R.string.no_connection),
				style = MaterialTheme.typography.titleLarge,
				color = MaterialTheme.colorScheme.primary,
				textAlign = TextAlign.Center
			)
			Button(
				modifier = Modifier
					.padding(
						horizontal = 50.dp,
						vertical = 16.dp
					)
					.fillMaxWidth(),
				onClick = {
					onRetry()
				}
			) {
				Text(
					text = stringResource(R.string.retry_prompt),
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
	}
}
