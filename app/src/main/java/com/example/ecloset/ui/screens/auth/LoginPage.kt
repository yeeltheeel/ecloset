package com.example.ecloset.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ecloset.ui.navigation.Routes
import com.example.ecloset.user.local.viewmodels.AuthViewModel

@Composable
fun LoginPage(
	modifier: Modifier = Modifier,
	navController: NavHostController,
	viewModel: AuthViewModel = hiltViewModel()
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
		item{
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
		}
		item{
			OutlinedTextField(
				label = { Text(stringResource(com.example.ecloset.R.string.email_prompt)) },
				value = loginFormState.email,
				shape = RoundedCornerShape(16.dp),
				onValueChange = {
					viewModel.updateLoginEmail(it)
				},
				modifier = Modifier.fillMaxWidth(),
				singleLine = true,
				isError = loginFormState.emailError != null,
				supportingText = loginFormState.emailError?.let {
						errId -> { Text(stringResource(errId)) }
				}
			)

			OutlinedTextField(
				label = { Text(stringResource(com.example.ecloset.R.string.password_prompt)) },
				value = loginFormState.password,
				shape = RoundedCornerShape(16.dp),
				onValueChange = {
					viewModel.updateLoginPassword(it)
				},
				modifier = Modifier.fillMaxWidth(),
				singleLine = true,
				isError = loginFormState.passwordError != null,
				supportingText = loginFormState.passwordError?.let {
						errId -> { Text(stringResource(errId)) }
				}
			)

			Text(
				text = stringResource(com.example.ecloset.R.string.forgot_password_prompt),
				modifier = Modifier
					.fillMaxWidth()
					.padding(
						horizontal = 16.dp,
						vertical = 8.dp
					)
					.clickable {
						navController.navigate(Routes.OTP) // otp to email action
					},
				style = MaterialTheme.typography.bodySmall,
				color = MaterialTheme.colorScheme.secondary
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
		item {
			Text(
				text = stringResource(com.example.ecloset.R.string.no_account_prompt),
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
				color = MaterialTheme.colorScheme.secondary,
				textAlign = TextAlign.Center
			)
		}
	}
}