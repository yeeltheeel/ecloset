@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.ecloset

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.TopAppBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ecloset.ui.theme.EclosetTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api

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
    val userAvatarUrl: String? = null
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {Text(text = stringResource(R.string.app_name))},
                actions = {
                    IconButton(
                        onClick = {
                            // Open and close the menu
                        },
                    ) {
                        if (userAvatarUrl == null) {
                            // default avatar when no image is available.
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "User profile",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            // display the user's image here later.
                            // Replace this branch with AsyncImage when using Coil.
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "User profile",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )}
    ) {
            innerPadding ->
        MainPage(
            modifier = Modifier.padding(innerPadding),
            onOpenGrid = {
                // This should navigate to the grid page
            },

            onCardClick = { card ->
                // This is called when an individual card is clicked
            }
        )
    }
}

data class PreviewCard(
    val id: Int,
    val title: String,
    val color: Color
)

@Composable
fun MainPage(
    modifier: Modifier = Modifier,
    onOpenGrid: () -> Unit,
    onCardClick: (PreviewCard) -> Unit
) {
    val previewOutfits = listOf(
        PreviewCard(
            id = 1,
            title = "Outfit 1",
            color = Color(0xFFE8DFF5)
        ),
        PreviewCard(
            id = 2,
            title = "Outfit 2",
            color = Color(0xFFDDEBF7)
        ),
        PreviewCard(
            id = 3,
            title = "Outfit 3",
            color = Color(0xFFFCE1E4)
        )
    )
    val previewClothes = listOf(
        PreviewCard(
            id = 1,
            title = "Item 1",
            color = Color(0xFFE8DFF5)
        ),
        PreviewCard(
            id = 2,
            title = "Item 2",
            color = Color(0xFFDDEBF7)
        ),
        PreviewCard(
            id = 3,
            title = "Item 3",
            color = Color(0xFFFCE1E4)
        )
    )
    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        item{
            PreviewCarousel(
                modifier = modifier,
                previewTitle = stringResource(R.string.outfit_list_title),
                cards = previewOutfits,
                onOpenGrid = {
                    // Navigate to the complete grid page
                    // navController.navigate("outfit_grid")
                },
                onCardClick = { card ->
                    // Open the selected card
                    // navController.navigate("outfit/${card.id}")
                }
            )
        }
        item{
            PreviewCarousel(
                modifier = modifier,
                previewTitle = stringResource(R.string.clothes_list_title),
                cards = previewClothes,
                onOpenGrid = {},
                onCardClick = { card -> }
            )
        }
    }
}

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

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    EclosetTheme {
//        Greeting(name = "Android")
//    }
//}