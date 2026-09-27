package com.example.ecloset.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun WeatherWidget(
    modifier: Modifier,
    location: String // active if enabled else set (?String)
) {
    Card(
        modifier = modifier
            .height(150.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(
            text = location,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                vertical = 8.dp,
                horizontal = 8.dp
            )
        )
    }
    Spacer(modifier = Modifier.padding(vertical = 8.dp))
}