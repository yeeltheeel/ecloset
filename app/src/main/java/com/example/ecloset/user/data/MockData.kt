package com.example.ecloset.user.data

import com.example.ecloset.R
import com.example.ecloset.user.local.ClosetItemData
import com.example.ecloset.user.local.OutfitItemData
import com.example.ecloset.user.local.UserInfo
import java.time.LocalDate

object MockData {
	val testUser = UserInfo(
		id = 1,
		username = "yeel",
		email = "yeeltheeel@github.com",
		birthday = LocalDate.of(2002, 1, 3),
		avatarUrl = null
	)

	/* closet  */
	val clothesList = listOf(
		ClosetItemData(
			id = 1,
			title = "White cotton t-shirt",
			imageUrl = null,
			category = 1,
			season = "Summer",
			material = "Cotton",
			color = 8,
			occasion = "Casual",
			dateAdded = LocalDate.of(2024, 1, 1)
		),
		ClosetItemData(
			id = 2,
			title = "Blue denim jeans",
			imageUrl = null,
			category = 2,
			season = "All",
			material = "Denim",
			color = 7,
			occasion = "Casual",
			dateAdded = LocalDate.of(2024, 1, 2)
		),
		ClosetItemData(
			id = 3,
			title = "Black leather jacket",
			imageUrl = null,
			category = 5,
			season = "Autumn",
			material = "Leather",
			color = 9,
			occasion = "Party",
			dateAdded = LocalDate.of(2024, 1, 3)
		),
		ClosetItemData(
			id = 4,
			title = "Floral summer dress",
			imageUrl = null,
			category = 1,
			season = "Summer",
			material = "Chiffon",
			color = 4,
			occasion = "Date",
			dateAdded = LocalDate.of(2024, 1, 4)
		),
		ClosetItemData(
			id = 5,
			title = "Grey wool sweater",
			imageUrl = null,
			category = 1,
			season = "Winter",
			material = "Wool",
			color = 10,
			occasion = "Casual",
			dateAdded = LocalDate.of(2024, 1, 5)
		),
		ClosetItemData(
			id = 6,
			title = "Beige chino pants",
			imageUrl = null,
			category = 2,
			season = "Spring",
			material = "Cotton",
			color = 2,
			occasion = "Work",
			dateAdded = LocalDate.of(2024, 1, 6)
		),
		ClosetItemData(
			id = 7,
			title = "Red silk blouse",
			imageUrl = null,
			category = 1,
			season = "Spring",
			material = "Silk",
			color = 5,
			occasion = "Work",
			dateAdded = LocalDate.of(2024, 1, 7)
		),
		ClosetItemData(
			id = 8,
			title = "Navy pencil skirt",
			imageUrl = null,
			category = 2,
			season = "All",
			material = "Polyester",
			color = 7,
			occasion = "Work",
			dateAdded = LocalDate.of(2024, 1, 8)
		),
		ClosetItemData(
			id = 9,
			title = "White sneakers",
			imageUrl = null,
			category = 3,
			season = "All",
			material = "Canvas",
			color = 8,
			occasion = "Casual",
			dateAdded = LocalDate.of(2024, 1, 9)
		),
		ClosetItemData(
			id = 10,
			title = "Brown trench coat",
			imageUrl = null,
			category = 5,
			season = "Autumn",
			material = "Cotton",
			color = 2,
			occasion = "Work",
			dateAdded = LocalDate.of(2024, 1, 10)
		)
	)

	/* outfits  */
	val outfitsList = listOf(
		OutfitItemData(
			id = 1,
			title = "Casual summer day",
			imageUrl = null,
			season = "Summer",
			occasion = "Casual",
			color = 8,
			dateAdded = LocalDate.of(2024, 1, 11)
		),
		OutfitItemData(
			id = 2,
			title = "Office chic",
			imageUrl = null,
			season = "Spring",
			occasion = "Work",
			color = 9,
			dateAdded = LocalDate.of(2024, 1, 12)
		),
		OutfitItemData(
			id = 3,
			title = "Autumn evening look",
			imageUrl = null,
			season = "Autumn",
			occasion = "Party",
			color = 5,
			dateAdded = LocalDate.of(2024, 1, 13)
		)
	)

	/* main */
	val previewOutfits = outfitsList.take(3)
	val previewClothes = clothesList.take(3)

	val previewUnusedClothes = emptyList<ClosetItemData>()

}