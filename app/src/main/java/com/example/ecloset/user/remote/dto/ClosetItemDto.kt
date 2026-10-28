package com.example.ecloset.user.remote.dto

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class ClosetItemDto(
	@SerializedName("id")
	val id: Int,
	@SerializedName("title")
	val title: String,
	@SerializedName("imageUrl")
	val imageUrl: String?,
	@SerializedName("category")
	val category: Int,
	@SerializedName("season")
	val season: String,
	@SerializedName("occasion")
	val occasion: String,
	@SerializedName("color")
	val color: Int,
	@SerializedName("material")
	val material: String,
	@SerializedName("dateAdded")
	val dateAdded: LocalDate
)
