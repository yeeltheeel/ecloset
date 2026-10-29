package com.example.ecloset.ui.screens.outfits

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ecloset.R
import androidx.navigation.NavHostController
import com.example.ecloset.ui.components.GridLayout
import com.example.ecloset.ui.components.PageHeader
import com.example.ecloset.user.data.MockData.outfitsList
//import com.example.ecloset.user.data.Presets.outfitsCategories

@Composable
fun OutfitPage(
	modifier: Modifier = Modifier,
	navController: NavHostController
){
	LazyColumn(
		modifier
			.fillMaxWidth(),
		contentPadding = PaddingValues(top = 0.dp)
	) {
		item{
			Spacer(modifier = Modifier
				.padding(vertical = 20.dp)
			)
			PageHeader(
				title = stringResource(R.string.outfit_list_title),
				navController = navController
			)
		}
		item{
//			GridHeader(
//				categoryList = outfitsCategories
//			)
			GridLayout(
				itemList = outfitsList
			)
		}
	}
}

