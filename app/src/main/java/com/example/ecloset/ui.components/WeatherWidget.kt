package com.example.ecloset.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material3.Icon

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
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp)
        ) {
            item {
                Icon(
                    imageVector = Icons.Default.Air,
                    contentDescription = "Weather icon",
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            item{
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(16.dp)
                ) {
                    Text(
                        text = location,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(
                            vertical = 0.dp,
                            horizontal = 8.dp
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "15" + "${'\u00B0'}C",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(
                            vertical = 8.dp,
                            horizontal = 8.dp
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.padding(vertical = 8.dp))
}