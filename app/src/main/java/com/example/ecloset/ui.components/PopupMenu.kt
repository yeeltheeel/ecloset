package com.example.ecloset.ui.components

import android.widget.PopupMenu
import androidx.compose.runtime.Composable

enum class PopupDirection {
	UP,
	DOWN
}

data class PopupAction(
	val title: String,
	val onClick: () -> Unit
)

@Composable
fun PopupMenu(){

}