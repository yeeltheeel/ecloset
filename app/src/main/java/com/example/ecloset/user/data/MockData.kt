package com.example.ecloset.user.data

import androidx.compose.material3.MaterialTheme
import com.example.ecloset.user.data.PreviewCard

object MockData {
	val testUser = UserInfo(
		id = 1,
		username = "User",
		email = "user@example.com",
		avatarUrl = null
	)

	/* main */
	val previewOutfits = listOf(
		PreviewCard(
			id = 1,
			title = "Outfit 1",
			type = "outfit"
		),
		PreviewCard(
			id = 2,
			title = "Outfit 2",
			type = "outfit"
		),
		PreviewCard(
			id = 3,
			title = "Outfit 3",
			type = "outfit"
		)
	)

	val previewClothes = listOf(
		PreviewCard(
			id = 1,
			title = "Item 1",
			type = "clothes"
		),
		PreviewCard(
			id = 2,
			title = "Item 2",
			type = "clothes"
		),
		PreviewCard(
			id = 3,
			title = "Item 3",
			type = "clothes"
		)
	)

	/* closet  */
	val clothesCategories = listOf(
		CategoryItem(
			id = 1,
			title = "Tops"
		),
		CategoryItem(
			id = 2,
			title = "Bottoms"
		),
		CategoryItem(
			id = 3,
			title = "Shoes"
		),
		CategoryItem(
			id = 4,
			title = "Accessories"
		)
	)

	val clothesList = listOf(
		PreviewCard(
			id = 1,
			title = "Item 1",
			type = "clothes"
		),
		PreviewCard(
			id = 2,
			title = "Item 2",
			type = "clothes"
		),
		PreviewCard(
			id = 3,
			title = "Item 3",
			type = "clothes"
		),
		PreviewCard(
			id = 4,
			title = "Item 4",
			type = "clothes"
		),
		PreviewCard(
			id = 5,
			title = "Item 5",
			type = "clothes"
		),
		PreviewCard(
			id = 6,
			title = "Item 6",
			type = "clothes"
		)
	)

	/* outfits  */
	val outfitsCategories = listOf(
		CategoryItem(
			id = 1,
			title = "Seasonal"
		),
		CategoryItem(
			id = 2,
			title = "Casual"
		),
		CategoryItem(
			id = 3,
			title = "Party"
		)
	)
	val outfitsList = listOf(
		PreviewCard(
			id = 1,
			title = "Outfit 1",
			type = "outfit"
		),
		PreviewCard(
			id = 2,
			title = "Outfit 2",
			type = "outfit"
		),
		PreviewCard(
			id = 3,
			title = "Outfit 3",
			type = "outfit"
		),
		PreviewCard(
			id = 4,
			title = "Outfit 4",
			type = "outfit"
		),
		PreviewCard(
			id = 5,
			title = "Outfit 5",
			type = "outfit"
		),
		PreviewCard(
			id = 6,
			title = "Outfit 6",
			type = "outfit"
		)
	)
}