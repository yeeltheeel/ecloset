package com.example.ecloset.ui.screens.closet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ecloset.R
import androidx.navigation.NavHostController
import com.example.ecloset.ui.components.GridHeader
import com.example.ecloset.ui.components.GridLayout
import com.example.ecloset.ui.components.PageHeader
import com.example.ecloset.user.data.MockData.clothesList
import com.example.ecloset.user.data.Presets.clothesCategories
import com.example.ecloset.user.local.ClosetError
import com.example.ecloset.user.local.viewmodels.ClosetItemViewModel

@Composable
fun ClosetPage(
	modifier: Modifier = Modifier,
	navController: NavHostController,
	snackbarHostState: SnackbarHostState,
	clothesViewModel: ClosetItemViewModel = hiltViewModel()
){
	val state by clothesViewModel.itemState.collectAsStateWithLifecycle()
	val closetError by clothesViewModel.closetError.collectAsStateWithLifecycle()

	val errorMessage: String? = closetError?.let { error ->
		when (error) {
			is ClosetError.Resource -> stringResource(error.stringRes)
			is ClosetError.Message  -> error.text
		}
	}

	LaunchedEffect(errorMessage) {
		errorMessage?.let { msg ->
			snackbarHostState.showSnackbar(msg)
			clothesViewModel.clearResult()
		}
	}

	Box(
		modifier = modifier
			.fillMaxSize()
	) {
		when {
			state.isLoading -> {
				CircularProgressIndicator(
					modifier = Modifier
						.align(Alignment.Center)
				)
			}
			state.error != null -> {
				Text(
					text = stringResource(state.error!!),
					modifier = Modifier.align(Alignment.Center)
				)
			}
			else -> {
				LazyColumn(
					modifier
						.fillMaxWidth(),
					contentPadding = PaddingValues(top = 0.dp)
				) {
					item {
						PageHeader(
							title = stringResource(R.string.clothes_list_title),
							navController = navController
						)
					}
					item {
						GridHeader(
							categoryList = clothesCategories
						)
						GridLayout(
							itemList = state.items // clothesList
						)
					}
				}
			}
		}
	}
}