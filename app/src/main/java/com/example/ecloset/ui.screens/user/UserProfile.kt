package com.example.ecloset.ui.screens.user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ecloset.R
import com.example.ecloset.ui.navigation.Routes
import com.example.ecloset.user.data.UserViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.style.TextAlign

@Composable
fun UserProfile(
	modifier: Modifier = Modifier,
	navController: NavHostController,
	userViewModel: UserViewModel
){
	LazyColumn(
		modifier
			.fillMaxWidth(),
		contentPadding = PaddingValues(top = 0.dp)
	) {
		item{
			Spacer(modifier = Modifier.padding(vertical = 20.dp))
			Text(
				text = "< " + stringResource(com.example.ecloset.R.string.back_prompt),
				modifier = Modifier
					.fillMaxWidth()
					.padding(
						horizontal = 16.dp,
						vertical = 8.dp
					)
					.clickable{
						navController.navigate(Routes.MAIN)
					},
				style = MaterialTheme.typography.bodySmall,
				color = MaterialTheme.colorScheme.secondary
			)
		}
		item {
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
		item{
			Text(
				text = stringResource(R.string.username_prompt),
				modifier = Modifier
					.fillMaxWidth()
					.padding(
						horizontal = 16.dp,
						vertical = 8.dp
					),
				style = MaterialTheme.typography.bodyLarge
			)
			Text(
				text = userViewModel.userState.collectAsState().value.user?.username ?: String(),
				modifier = Modifier
					.fillMaxWidth()
					.padding(
						horizontal = 16.dp,
						vertical = 4.dp
					),
				style = MaterialTheme.typography.bodyMedium,
				color = MaterialTheme.colorScheme.tertiary
			)
			Spacer(modifier = Modifier.padding(vertical = 8.dp))
		}
		item{
			Text(
				text = stringResource(R.string.email_prompt),
				modifier = Modifier
					.fillMaxWidth()
					.padding(
						horizontal = 16.dp,
						vertical = 8.dp
					),
				style = MaterialTheme.typography.bodyLarge
			)
			Text(
				text = userViewModel.userState.collectAsState().value.user?.email ?: String(),
				modifier = Modifier
					.fillMaxWidth()
					.padding(
						horizontal = 16.dp,
						vertical = 4.dp
					),
				style = MaterialTheme.typography.bodyMedium,
				color = MaterialTheme.colorScheme.tertiary
			)
			Spacer(modifier = Modifier.padding(vertical = 8.dp))
		}
		item{
			Button(
				modifier = Modifier
					.fillMaxWidth()
					.padding(
						horizontal = 50.dp,
						vertical = 4.dp
					),
				onClick = {
					userViewModel.logOut()
				}
			) {
				Text(
					text = stringResource(com.example.ecloset.R.string.logout_promt),
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
			Button(
				modifier = Modifier
					.fillMaxWidth()
					.padding(
						horizontal = 50.dp,
						vertical = 4.dp
					),
				onClick = {
					userViewModel.deleteAccount()
				}
			) {
				Text(
					text = stringResource(com.example.ecloset.R.string.delete_account_prompt),
					modifier = Modifier
						.fillMaxWidth()
						.padding(
							horizontal = 16.dp,
							vertical = 12.dp
						),
					style = MaterialTheme.typography.titleLarge,
					color = MaterialTheme.colorScheme.error,
					textAlign = TextAlign.Center
				)
			}
		}
	}
}

