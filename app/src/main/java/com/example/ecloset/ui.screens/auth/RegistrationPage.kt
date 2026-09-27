package com.example.ecloset.ui.screens.auth

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign

@Composable
fun RegistrationPage(){
	Text(
		text = stringResource(com.example.ecloset.R.string.register_title),
		textAlign = TextAlign.Left
	)
}