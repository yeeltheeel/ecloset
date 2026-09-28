package com.example.ecloset.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ecloset.R

@Composable
fun UserProfile(){
	Text(
		text = stringResource(com.example.ecloset.R.string.back_prompt),
		modifier = Modifier
			.fillMaxWidth()
			.padding(
				horizontal = 16.dp,
				vertical = 8.dp
			)
			.clickable{},
		style = MaterialTheme.typography.bodySmall,
		color = MaterialTheme.colorScheme.secondary
	)
	Spacer(modifier = Modifier.padding(vertical = 8.dp))
	Text(
		text = stringResource(R.string.user_profile_title),
		modifier = Modifier
			.fillMaxWidth()
			.padding(
				horizontal = 16.dp,
				vertical = 12.dp
			),
		style = MaterialTheme.typography.titleLarge,
		color = MaterialTheme.colorScheme.primary
	)
	Spacer(modifier = Modifier.padding(vertical = 8.dp))
}