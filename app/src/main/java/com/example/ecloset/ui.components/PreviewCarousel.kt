package com.example.ecloset.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import com.example.ecloset.ui.theme.EclosetTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ecloset.PreviewCard

data class PreviewCard(
    val id: Int,
    val title: String,
    val color: Color
)

@Composable
fun PreviewCarousel(
    modifier: Modifier,
    previewTitle: String,
    cards: List<PreviewCard>,
    onOpenGrid: () -> Unit,
    onCardClick: (PreviewCard) -> Unit
){
    // Clickable title above the carousel
    Text(
        text = previewTitle,
        modifier = Modifier
            .fillMaxWidth().clickable {
                onOpenGrid()
            }
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            ),
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.primary
    )
    // Horizontal card carousel
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 16.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = cards,
            key = { card -> card.id }
        ) { card ->
            PreviewOutfitCard(
                card = card,
                onClick = {
                    onCardClick(card)
                }
            )
        }
    }
}

@Composable
fun PreviewOutfitCard(
    card: PreviewCard,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .width(160.dp)
            .height(210.dp),
        colors = CardDefaults.cardColors(
            containerColor = card.color
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Temporary image placeholder
            Spacer(
                modifier = Modifier
                    .size(130.dp)
                    //.weight(1f)
                    .padding(bottom = 8.dp)
            )

            Text(
                text = card.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1
            )
        }
    }
}