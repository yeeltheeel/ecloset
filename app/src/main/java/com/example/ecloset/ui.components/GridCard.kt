package com.example.ecloset.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.ecloset.user.data.GridCardData

@Composable
fun GridCard(
	data: GridCardData
){
	Text(text = data.id.toString())
	Text(text = data.dateAdded.toString())
}
