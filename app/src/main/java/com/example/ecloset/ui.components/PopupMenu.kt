package com.example.ecloset.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties

enum class PopupDirection {
	UP, DOWN, LEFT, RIGHT
}

data class PopupAction(
	val content: String,
	val navRoute: String
)

private class PopupMenuPositionProvider(
	private val direction: PopupDirection,
	private val gap: Int = 8
) : PopupPositionProvider {
	override fun calculatePosition(
		anchorBounds: IntRect,
		windowSize: IntSize,
		layoutDirection: LayoutDirection,
		popupContentSize: IntSize
	): IntOffset {
		val anchorCenterX = anchorBounds.left + anchorBounds.width / 2
		val anchorCenterY = anchorBounds.top + anchorBounds.height / 2

		val rawX: Int
		val rawY: Int

		when(direction){
			PopupDirection.UP -> {
				rawX = anchorCenterX - popupContentSize.width / 2
				rawY = anchorBounds.top - popupContentSize.height - gap
			}
			PopupDirection.DOWN -> {
				rawX = anchorCenterX - popupContentSize.width / 2
				rawY = anchorBounds.bottom + gap
			}
			PopupDirection.LEFT -> {
				rawX = if (layoutDirection == LayoutDirection.Ltr) {
					anchorBounds.left - popupContentSize.width - gap
				} else {
					anchorBounds.right + gap
				}
				rawY = anchorCenterY - popupContentSize.height / 2
			}
			PopupDirection.RIGHT -> {
				rawX = if (layoutDirection == LayoutDirection.Ltr) {
					anchorBounds.right + gap
				} else {
					anchorBounds.left - popupContentSize.width - gap
				}
				rawY = anchorCenterY - popupContentSize.height / 2
			}
		}

		val horizontalMargin = 8
		val verticalMargin = 8

		val maxX = (windowSize.width - popupContentSize.width - horizontalMargin)
			.coerceAtLeast(horizontalMargin)
		val maxY = (windowSize.height - popupContentSize.height - verticalMargin)
			.coerceAtLeast(verticalMargin)

		return IntOffset(
			x = rawX.coerceIn(horizontalMargin, maxX),
			y = rawY.coerceIn(verticalMargin, maxY)
		)
	}
}

@Composable
fun PopupActionMenu(
	modifier: Modifier,
	expanded: Boolean,
	direction: PopupDirection,
	onDismiss: () -> Unit,
	actions: List<PopupAction>,
	navController: NavHostController
){
	if (!expanded) return
	val menuPos = remember(direction){
		PopupMenuPositionProvider(direction)
	}
	Popup(
		popupPositionProvider = menuPos,
		onDismissRequest = onDismiss,
		properties = PopupProperties(
			focusable = true,
			dismissOnBackPress = true,
			clippingEnabled = true
		)
	) {
		Surface(
			modifier = modifier,
			shape = RoundedCornerShape(12.dp),
			color = MaterialTheme.colorScheme.surface,
			tonalElevation = 6.dp,
			shadowElevation = 6.dp,
		) {
			Column(
				modifier = Modifier
					.width(200.dp)
					.clip(RoundedCornerShape(8.dp))
					.padding(
						vertical = 4.dp,
						horizontal = 8.dp
					),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				actions.forEach { action ->
					DropdownMenuItem(
						text = {
							Text(
								text = action.content,
								textAlign = TextAlign.Center
							)
						},
						onClick = {
							onDismiss()
							navController.navigate(action.navRoute)
						}
					)
				}
			}
		}
	}
}