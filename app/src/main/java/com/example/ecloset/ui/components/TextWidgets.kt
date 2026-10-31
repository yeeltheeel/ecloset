package com.example.ecloset.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ecloset.R

@Composable
fun LabelText(
	modifier: Modifier = Modifier,
	text: String,
	icon: ImageVector = Icons.Default.Circle,
	color: Color = MaterialTheme.colorScheme.primary,
	style: TextStyle =  MaterialTheme.typography.bodyMedium
){
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(4.dp)
	) {
		Icon(
			imageVector = icon,
			contentDescription = null,
			modifier = modifier
				.size(16.dp),
			tint = color
		)
		Text(
			text = text,
			style = style
		)
	}
}

@Composable
fun GroupTitle(
	title: String,
	caption: String = "",
	showCaption: Boolean = false
){
	Spacer(modifier = Modifier.padding(vertical = 8.dp))
	Text(
		text = title,
		modifier = Modifier
			.fillMaxWidth()
			.padding(
				horizontal = 16.dp,
				vertical = 8.dp
			),
		style = MaterialTheme.typography.titleMedium
	)
	if (showCaption){
		Text(
			text = caption,
			modifier = Modifier
				.fillMaxWidth()
				.padding(
					horizontal = 16.dp,
					vertical = 0.dp
				),
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.tertiary
		)
		Spacer(modifier = Modifier.padding(vertical = 8.dp))
	}
}

@Composable
fun EditableLabel(
	title: String,
	value: String,
	onClick: (String) -> Unit
){
	var newValue = value
	var expanded by remember { mutableStateOf(false) }

	Text(
		text = title,
		modifier = Modifier
			.fillMaxWidth()
			.padding(
				horizontal = 16.dp,
				vertical = 8.dp
			)
			.clickable{
				expanded = !expanded
			},
		style = MaterialTheme.typography.bodyLarge
	)
	if (!expanded) {
		Text(
			text = value,
			modifier = Modifier
				.fillMaxWidth()
				.padding(
					horizontal = 16.dp,
					vertical = 0.dp
				),
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.tertiary
		)
	} else {
		OutlinedTextField(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 8.dp, vertical = 8.dp),
			value = value,
			onValueChange = { newValue = it },
			shape = RoundedCornerShape(16.dp),
			singleLine = true,
			placeholder = {
				Text(stringResource(R.string.new_value_prompt))
			}
		)

		Button(
			modifier = Modifier
				.fillMaxWidth()
				.padding(
					horizontal = 90.dp,
					vertical = 8.dp
				),
			onClick = {
				expanded = !expanded
				onClick(newValue)
			}
		) {
			Text(
				text = stringResource(R.string.save_prompt),
				modifier = Modifier
					.fillMaxWidth()
					.padding(
						horizontal = 12.dp,
						vertical = 8.dp
					),
				style = MaterialTheme.typography.bodyMedium,
				color = MaterialTheme.colorScheme.onPrimary,
				textAlign = TextAlign.Center
			)
		}
	}

	Spacer(modifier = Modifier.padding(vertical = 8.dp))
}
