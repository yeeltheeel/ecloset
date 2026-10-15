package com.example.ecloset.ui.screens.main

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.ecloset.R

@Composable
fun NoConnectionPage(
	modifier: Modifier
){
	Text(
		text = stringResource(R.string.no_connection),
		style = MaterialTheme.typography.titleLarge,
		color = MaterialTheme.colorScheme.primary
	)
}
