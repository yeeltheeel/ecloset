package com.example.ecloset.user.remote.dto

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class OutfitItemDto(
	@SerializedName("id")
	val id: Int,
	@SerializedName("title")
	val title: String,
	@SerializedName("imageUrl")
	val imageUrl: String?,
	@SerializedName("season")
	val season: String,
	@SerializedName("occasion")
	val occasion: String,
	@SerializedName("color")
	val color: Int,
	@SerializedName("dateAdded")
	val dateAdded: LocalDate
)