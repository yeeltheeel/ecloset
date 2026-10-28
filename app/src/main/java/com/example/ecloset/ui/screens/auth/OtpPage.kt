package com.example.ecloset.ui.screens.auth

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ecloset.ui.components.PageHeader
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.ui.unit.Dp
import com.example.ecloset.ui.navigation.Routes
import com.example.ecloset.user.local.AuthViewModel
import com.example.ecloset.user.local.CODE_LEN

@Composable
fun OtpPage(
	modifier: Modifier = Modifier,
	navController: NavHostController,
	backAddredss: String = Routes.LOGIN,
	viewModel: AuthViewModel = viewModel()
) {
	val loginFormState by viewModel.loginFormState.collectAsState()
	val authResult by viewModel.authResult.collectAsState()

	LazyColumn(
		modifier = modifier.fillMaxWidth(),
		contentPadding = PaddingValues(vertical = 0.dp, horizontal = 16.dp)
	) {
		item {
			PageHeader(
				title = stringResource(com.example.ecloset.R.string.otp_title),
				backAddress = backAddredss,
				navController = navController
			)
		}
		item {
			OtpInputField(
				label = { Text(stringResource(com.example.ecloset.R.string.code_prompt)) },
				value = loginFormState.confirmCode ?: "",
				shape = RoundedCornerShape(16.dp),
				onValueChange = { viewModel.updateLoginCode(it) },
				modifier = Modifier.fillMaxWidth(),
				singleLine = true,
				isError = loginFormState.codeError != null,
				supportingText = loginFormState.codeError?.let { errId ->
					{ Text(stringResource(errId)) }
				}
			)
			Spacer(modifier = Modifier.padding(vertical = 8.dp))

			Button(
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 50.dp, vertical = 4.dp),
				onClick = { viewModel.login() }
			) {
				Text(
					text = stringResource(com.example.ecloset.R.string.confirm_prompt),
					modifier = Modifier
						.fillMaxWidth()
						.padding(horizontal = 16.dp, vertical = 12.dp),
					style = MaterialTheme.typography.titleLarge,
					color = MaterialTheme.colorScheme.onPrimary,
					textAlign = TextAlign.Center
				)
			}
		}
	}
}

@Composable
fun OtpInputField(
	value: String,
	onValueChange: (String) -> Unit,
	modifier: Modifier = Modifier,
	enabled: Boolean = true,
	label: (@Composable () -> Unit)? = null,
	placeholder: (@Composable () -> Unit)? = null,
	supportingText: (@Composable () -> Unit)? = null,
	isError: Boolean = false,
	singleLine: Boolean = true,
	length: Int = CODE_LEN,
	shape: Shape = RoundedCornerShape(16.dp),
	// Matches Material 3 OutlinedTextField default height
	cellHeight: Dp = 56.dp,
	// Width of each cell (mini text-field)
	cellWidth: Dp = 48.dp,
	// Spacing between cells
	cellSpacing: Dp = 8.dp
) {
	val focusRequester = remember { FocusRequester() }
	val interactionSource = remember { MutableInteractionSource() }
	val isFocused by interactionSource.collectIsFocusedAsState()

	// Thin outline (1.dp) normally, 2.dp when focused, error color on error
	val cellBorderColor = when {
		isError   -> MaterialTheme.colorScheme.error
		isFocused -> MaterialTheme.colorScheme.primary
		else      -> MaterialTheme.colorScheme.outline
	}
	val cellBorderWidth = if (isFocused) 2.dp else 1.dp

	Column(modifier = modifier) {

		// ---- Label ----
		if (label != null) {
			CompositionLocalProvider(
				LocalContentColor provides if (isError)
					MaterialTheme.colorScheme.error
				else
					MaterialTheme.colorScheme.onSurfaceVariant
			) {
				Box(modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)) {
					label()
				}
			}
		}

		// ---- Digit cells + invisible input, centered ----
		Box(
			modifier = Modifier.fillMaxWidth(),
			contentAlignment = Alignment.Center
		) {
			BasicTextField(
				value = value,
				onValueChange = { new ->
					if (new.length <= length && new.all { it.isDigit() }) {
						onValueChange(new)
					}
				},
				enabled = enabled,
				singleLine = singleLine,
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.NumberPassword
				),
				interactionSource = interactionSource,
				cursorBrush = SolidColor(Color.Transparent),
				modifier = Modifier
					.matchParentSize()
					.alpha(0f)
					.focusRequester(focusRequester)
			)

			Row(
				horizontalArrangement = Arrangement.spacedBy(
					cellSpacing,
					Alignment.CenterHorizontally
				),
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.height(cellHeight)
					.wrapContentWidth(Alignment.CenterHorizontally)
			) {
				repeat(length) { index ->
					val char = value.getOrNull(index)?.toString().orEmpty()
					val isCurrent = isFocused &&
							index == value.length.coerceAtMost(length - 1)

					Box(
						contentAlignment = Alignment.Center,
						modifier = Modifier
							.width(cellWidth)
							.fillMaxSize()
							.border(
								width = if (isCurrent) 2.dp else cellBorderWidth,
								color = if (isCurrent)
									MaterialTheme.colorScheme.primary
								else
									cellBorderColor,
								shape = shape
							)
					) {
						if (char.isEmpty() && placeholder != null && index == 0) {
							placeholder()
						} else {
							Text(
								text = char,
								style = MaterialTheme.typography.headlineSmall,
								textAlign = TextAlign.Center
							)
						}
					}
				}
			}
		}

		// ---- Supporting text ----
		if (supportingText != null) {
			CompositionLocalProvider(
				LocalContentColor provides if (isError)
					MaterialTheme.colorScheme.error
				else
					MaterialTheme.colorScheme.onSurfaceVariant
			) {
				Box(modifier = Modifier.padding(start = 4.dp, top = 4.dp)) {
					supportingText()
				}
			}
		}
	}

	LaunchedEffect(Unit) { focusRequester.requestFocus() }
}