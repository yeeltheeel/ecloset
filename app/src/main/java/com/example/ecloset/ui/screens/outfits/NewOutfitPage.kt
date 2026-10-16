package com.example.ecloset.ui.screens.outfits

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ecloset.R
import com.example.ecloset.ui.components.PageHeader
import com.example.ecloset.ui.navigation.Routes

@Composable
fun NewOutfitPage(
	modifier: Modifier = Modifier,
	navController: NavHostController
){
	LazyColumn(
		modifier
			.fillMaxWidth(),
		contentPadding = PaddingValues(top = 0.dp)
	) {
		item{
			PageHeader(
				title = stringResource(R.string.new_outfit_title),
				navController = navController
			)
		}
		item {
			Text(
				text = stringResource(R.string.occasion_prompt),
				modifier = Modifier
					.fillMaxWidth()
					.padding(
						horizontal = 16.dp,
						vertical = 8.dp
					)
					.clickable{},
				style = MaterialTheme.typography.bodyLarge
			)
		}
		item {
			Text(
				text = stringResource(R.string.location_prompt),
				modifier = Modifier
					.fillMaxWidth()
					.padding(
						horizontal = 16.dp,
						vertical = 8.dp
					)
					.clickable{},
				style = MaterialTheme.typography.bodyLarge
			)
		}
	}
}