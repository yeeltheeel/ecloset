package com.example.ecloset.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ecloset.ui.navigation.Routes
import com.example.ecloset.user.data.AuthViewModel

@Composable
fun OtpPage(
	modifier: Modifier = Modifier,
	navController: NavHostController,
	viewModel: AuthViewModel = viewModel()
){
	val loginFormState by viewModel.loginFormState.collectAsState()
	val authResult by viewModel.authResult.collectAsState()

	LazyColumn(
		modifier = modifier
			.fillMaxWidth(),
		contentPadding = PaddingValues(
			vertical = 60.dp,
			horizontal = 16.dp
		)
	) {
		item {
			Text(
				text = stringResource(com.example.ecloset.R.string.otp_title),
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
		item {
			OutlinedTextField(
				value = loginFormState.confirmCode ?: "",
				onValueChange = {
					viewModel.updateLoginField("code", it)
				},
				label = { Text(stringResource(com.example.ecloset.R.string.code_prompt)) },
				modifier = Modifier.fillMaxWidth(),
				singleLine = true,
				isError = loginFormState.codeError != null,
				supportingText = loginFormState.codeError?.let {
					errId -> { Text(stringResource(errId)) }
				}
			)
			Spacer(modifier = Modifier.padding(vertical = 8.dp))
			Button(
				modifier = Modifier
					.fillMaxWidth()
					.padding(
						horizontal = 50.dp,
						vertical = 4.dp
					),
				onClick = {
					viewModel.login()
				}
			) {
				Text(
					text = stringResource(com.example.ecloset.R.string.confirm_prompt),
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
		item {
			Text(
				text = "< " + stringResource(com.example.ecloset.R.string.back_prompt),
				modifier = Modifier
					.fillMaxWidth()
					.padding(
						horizontal = 16.dp,
						vertical = 8.dp
					)
					.clickable {
						navController.navigate(Routes.REGISTER)
					},
				style = MaterialTheme.typography.bodySmall,
				color = MaterialTheme.colorScheme.secondary
			)
		}
	}
}
