package com.example.ecloset.ui.screens.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ecloset.R
import com.example.ecloset.ui.components.DonutChart
import com.example.ecloset.ui.components.PreviewSingleRow
import com.example.ecloset.user.data.Presets.clothesCategories
import com.example.ecloset.user.local.viewmodels.AnalyticsViewmodel

@Composable
fun AnalyticsPage(
	modifier: Modifier = Modifier,
	analyticsViewmodel: AnalyticsViewmodel = hiltViewModel()
){
	val clothesList by analyticsViewmodel.clothesList.collectAsStateWithLifecycle()
	val previewUnusedClothes by analyticsViewmodel.unusedClothes.collectAsStateWithLifecycle()

	LazyColumn(
		modifier
			.fillMaxWidth(),
		contentPadding = PaddingValues(top = 0.dp)
	) {
		item{
			Text(
				text = stringResource(R.string.analytics_title),
				modifier = Modifier
					.fillMaxWidth()
					.padding(
						horizontal = 16.dp,
						vertical = 12.dp
					)
					.padding(top = 35.dp),
				style = MaterialTheme.typography.titleLarge,
				color = MaterialTheme.colorScheme.primary
			)
			DonutChart(
				categories = clothesCategories,
				items = clothesList.items
			)

			// add most worn color label or color ranking

			Spacer(modifier = Modifier.padding(vertical = 8.dp))
		}
		item{
			Text(
				text = stringResource(R.string.unused_items_title),
				modifier = Modifier
					.fillMaxWidth()
					.padding(
						horizontal = 16.dp,
						vertical = 12.dp
					),
				style = MaterialTheme.typography.titleLarge,
				color = MaterialTheme.colorScheme.primary
			)
			Text(
				text = stringResource(com.example.ecloset.R.string.unused_items_text),
				modifier = Modifier
					.fillMaxWidth()
					.padding(
						horizontal = 16.dp,
						vertical = 8.dp
					)
			)
			PreviewSingleRow(
				cards =  previewUnusedClothes.items
			)
		}
		item{
			Spacer(modifier = Modifier.padding(vertical = 70.dp))
		}
	}
}