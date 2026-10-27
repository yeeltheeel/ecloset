package com.example.ecloset.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.ecloset.R
import com.example.ecloset.user.data.CategoryItem
import java.io.Serializable

@Composable
fun RadioGroupBlock(
	options: List<String>, // CategoryItems ???
	selectedIndex: Int,
	onOptionSelected: (Int) -> Unit,
){
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.selectableGroup()
			.padding(
				horizontal = 16.dp,
				vertical = 8.dp
			)
	){
		options.forEachIndexed { index, option ->
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(
						vertical = 8.dp
					)
					.selectable(
						selected = (index == selectedIndex),
						onClick = {
							onOptionSelected(index)
						},
						role = Role.RadioButton
					),
				verticalAlignment = Alignment.CenterVertically
			){
				RadioButton(
					selected = (index == selectedIndex),
					onClick = null
				)
				Text(
					text = option,
					style = MaterialTheme.typography.bodyMedium,
					modifier = Modifier
						.padding(
							horizontal = 8.dp
						)
				)
			}
		}
	}
}

@Composable
fun RadioGroupWithDropdown(
	options: List<String>,
	items: List<String>, // CategoryItems ???
	selectedIndex: Int,
	selectedItem: Int?,
	onOptionSelected: (Int) -> Unit,
	onItemSelected: (Int) -> Unit,
	dropIndex: Int,
	searchThreshold: Int = 25
){
	val showDropdown = (selectedIndex == dropIndex)

	Column(
		modifier = Modifier
			.fillMaxWidth()
			.selectableGroup()
			.padding(
				horizontal = 16.dp,
				vertical = 8.dp
			)
	){
		options.forEachIndexed { index, option ->
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(
						vertical = 8.dp
					)
					.selectable(
						selected = (index == selectedIndex),
						onClick = {
							onOptionSelected(index)
						},
						role = Role.RadioButton
					),
				verticalAlignment = Alignment.CenterVertically
			){
				RadioButton(
					selected = (index == selectedIndex),
					onClick = null
				)
				Spacer(modifier = Modifier.width(8.dp))
				Text(
					text = option,
					style = MaterialTheme.typography.bodyMedium,
					modifier = Modifier
				)
			}

			if (index == dropIndex && showDropdown) {
				DropdownList(
					items = items,
					selectedItem = selectedItem,
					onItemSelected = onItemSelected,
					searchThreshold = searchThreshold
				)
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownList(
	items: List<String>,
	selectedItem: Int?,
	onItemSelected: (Int) -> Unit,
	searchThreshold: Int
) {
	var expanded by remember { mutableStateOf(false) }

	var searchQuery by remember(items) { mutableStateOf("") }
	val visibleItems: List<Pair<Int, String>> = remember(items, searchQuery) {
		if (searchQuery.isBlank())
			items.withIndex().map {
				it.index to it.value
			}
		else items.withIndex()
			.filter { (_, s) ->
				s.contains(searchQuery, ignoreCase = true)
			}.map {
				it.index to it.value
			}
	}
	val count = visibleItems.size

	ExposedDropdownMenuBox(
		modifier = Modifier
			.fillMaxWidth()
			.padding(
				vertical = 16.dp,
				horizontal = 8.dp
			),
		expanded = expanded,
		onExpandedChange = { expanded = !expanded }
	) {
		OutlinedTextField(
			modifier = Modifier
				.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
				.fillMaxWidth(),
			value = selectedItem?.let { items.getOrNull(it) } ?: "Select…",
			onValueChange = {},
			readOnly = true,
			trailingIcon = {
				ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
			},
			shape = RoundedCornerShape(16.dp)
		)

		ExposedDropdownMenu(
			expanded = expanded,
			onDismissRequest = {
				expanded = false
			}
		) {
			if (count > searchThreshold){
				OutlinedTextField(
					modifier = Modifier
						.fillMaxWidth()
						.padding(horizontal = 8.dp, vertical = 8.dp),
					value = searchQuery,
					onValueChange = { searchQuery = it },
					shape = RoundedCornerShape(16.dp),
					singleLine = true,
					placeholder = { Text(stringResource(R.string.search_prompt)) }
				)
			}

			if (visibleItems.isEmpty()) {
				DropdownMenuItem(
					text = { Text(stringResource(R.string.no_match)) },
					onClick = {},
					enabled = false
				)
			} else {
				visibleItems.forEach { (sourceIndex, label) ->
					DropdownMenuItem(
						text = { Text(label) },
						onClick = {
							onItemSelected(sourceIndex)
							expanded = false
							searchQuery = ""   // reset for next open
						}
					)
				}
			}
		}
	}
}