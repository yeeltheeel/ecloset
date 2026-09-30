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
fun RegistrationPage(
	modifier: Modifier = Modifier,
	navController: NavHostController,
	viewModel: AuthViewModel = viewModel()
){
	val regFormState by viewModel.regFormState.collectAsState()
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
				text = stringResource(com.example.ecloset.R.string.register_title),
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
				value = regFormState.username,
				onValueChange = {
					viewModel.updateRegField("username", it)
				},
				label = { Text(stringResource(com.example.ecloset.R.string.username_prompt)) },
				modifier = Modifier.fillMaxWidth(),
				singleLine = true,
				isError = regFormState.usernameError != null,
				supportingText = regFormState.usernameError?.let {
						errId -> { Text(stringResource(errId)) }
				}
			)

			OutlinedTextField(
				value = regFormState.email,
				onValueChange = {
					viewModel.updateRegField("email", it)
				},
				label = { Text(stringResource(com.example.ecloset.R.string.email_prompt)) },
				modifier = Modifier.fillMaxWidth(),
				singleLine = true,
				isError = regFormState.emailError != null,
				supportingText = regFormState.emailError?.let {
						errId -> { Text(stringResource(errId)) }
				}
			)

			OutlinedTextField(
				value = regFormState.password,
				onValueChange = {
					viewModel.updateRegField("password", it)
				},
				label = { Text(stringResource(com.example.ecloset.R.string.password_prompt)) },
				modifier = Modifier.fillMaxWidth(),
				singleLine = true,
				isError = regFormState.passwordError != null,
				supportingText = regFormState.passwordError?.let {
						errId -> { Text(stringResource(errId)) }
				}
			)

			OutlinedTextField(
				value = regFormState.confirmPassword,
				onValueChange = {
					viewModel.updateRegField("confirmPassword", it)
				},
				label = { Text(stringResource(com.example.ecloset.R.string.repeat_password_prompt)) },
				modifier = Modifier.fillMaxWidth(),
				singleLine = true,
				isError = regFormState.confirmPasswordError != null,
				supportingText = regFormState.confirmPasswordError?.let {
						errId -> { Text(stringResource(errId)) }
				}
			)

			Button(
				modifier = Modifier
					.fillMaxWidth()
					.padding(
						horizontal = 50.dp,
						vertical = 4.dp
					),
				onClick = {
					// gray out, add loading animation
					viewModel.register()
				}
			) {
				Text(
					text = stringResource(com.example.ecloset.R.string.reg_prompt),
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
		item{
			Text(
				text = stringResource(com.example.ecloset.R.string.already_registered_prompt),
				modifier = Modifier
					.fillMaxWidth()
					.padding(
						horizontal = 16.dp,
						vertical = 8.dp
					)
					.clickable{
						navController.navigate(Routes.LOGIN)
					},
				style = MaterialTheme.typography.bodySmall,
				color = MaterialTheme.colorScheme.secondary
			)
		}
	}
}