package com.example.ecloset.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ecloset.user.data.LoginFormState
import com.example.ecloset.user.data.AuthResult

@Composable
fun LoginPage(){
	Text(
		text = stringResource(com.example.ecloset.R.string.login_title),
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
	Text(
		text = stringResource(com.example.ecloset.R.string.email_prompt),
		modifier = Modifier
			.fillMaxWidth()
			.padding(
				horizontal = 16.dp,
				vertical = 8.dp
			)
	)
	Text(
		text = stringResource(com.example.ecloset.R.string.password_prompt),
		modifier = Modifier
			.fillMaxWidth()
			.padding(
				horizontal = 16.dp,
				vertical = 8.dp
			)
	)
	Text(
		text = stringResource(com.example.ecloset.R.string.forgot_password_prompt),
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
	Button(
		modifier = Modifier
			.fillMaxWidth()
			.padding(
				horizontal = 50.dp,
				vertical = 8.dp
			),
		onClick = {}
	) {
		Text(
			text = stringResource(com.example.ecloset.R.string.login_prompt),
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