package com.example.ecloset.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun WeatherWidget(
    modifier: Modifier,
    location: String // active if enabled else set (?String)
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            ),
        propagateMinConstraints = true,
        contentAlignment = Alignment.Center
    ){
        Text(
            text = location,
            textAlign = TextAlign.Center
        )
    }
}