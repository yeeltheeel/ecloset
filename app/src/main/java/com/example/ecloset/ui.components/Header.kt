package com.example.ecloset.ui.components
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.ecloset.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(){
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
    ){ }
}