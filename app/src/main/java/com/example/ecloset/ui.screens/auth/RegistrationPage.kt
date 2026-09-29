package com.example.ecloset.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ecloset.ui.navigation.Routes
import com.example.ecloset.user.data.RegFormState
import com.example.ecloset.user.data.AuthResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel() : ViewModel() {
	private val regFormStateFlow = MutableStateFlow(RegFormState())
	val regFormState: StateFlow<RegFormState> = regFormStateFlow.asStateFlow()

	private val authResultFlow = MutableStateFlow<AuthResult?>(null)
	val authResult: StateFlow<AuthResult?> = authResultFlow.asStateFlow()

	fun onUsernameChange(it: String) { }
}

@Composable
fun RegistrationPage(
	modifier: Modifier = Modifier,
	navController: NavHostController,
	viewModel: AuthViewModel = viewModel()
){
	val regFormState by viewModel.regFormState.collectAsState()
	val authResult by viewModel.authResult.collectAsState()

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

//	Text(
//		text = stringResource(com.example.ecloset.R.string.username_prompt),
//		modifier = Modifier
//			.fillMaxWidth()
//			.padding(
//				horizontal = 16.dp,
//				vertical = 8.dp
//			)
//	)
	OutlinedTextField(
		value = regFormState.username,
		onValueChange = { viewModel.onUsernameChange(it) },
		label = { Text(stringResource(com.example.ecloset.R.string.username_prompt)) },
		modifier = Modifier.fillMaxWidth(),
		singleLine = true,
		isError = regFormState.usernameError != null
	)

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
		text = stringResource(com.example.ecloset.R.string.password_prompt),
		modifier = Modifier
			.fillMaxWidth()
			.padding(
				horizontal = 16.dp,
				vertical = 8.dp
			)
	)

	Text(
		text = stringResource(com.example.ecloset.R.string.repeat_password_prompt),
		modifier = Modifier
			.fillMaxWidth()
			.padding(
				horizontal = 16.dp,
				vertical = 8.dp
			)
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