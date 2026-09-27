@file:OptIn(ExperimentalMaterial3Api::class)
@file:SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")

package com.example.ecloset

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.ecloset.ui.theme.EclosetTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.ui.unit.dp
import com.example.ecloset.ui.components.ActionBarMenu
import com.example.ecloset.ui.components.Header
import com.example.ecloset.ui.screens.main.MainPage

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			EclosetTheme {
				EclosetScreen()
			}
		}
	}
}

@Composable
fun EclosetScreen(){
	Scaffold(
		modifier = Modifier.fillMaxSize(),
		topBar = { Header() },
		floatingActionButton = { ActionBarMenu() },
		floatingActionButtonPosition = FabPosition.Center
	) {
		innerPadding ->
		MainPage(
			modifier = Modifier
				.fillMaxSize()
				.padding(
					top = 70.dp
				)
		)
	}
}
