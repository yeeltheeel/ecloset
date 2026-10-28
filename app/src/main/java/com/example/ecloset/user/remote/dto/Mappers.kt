package com.example.ecloset.user.remote.dto

import com.example.ecloset.user.local.ClosetItemData
import com.example.ecloset.user.local.OutfitItemData
import com.example.ecloset.user.local.UserInfo

fun UserDto.toUi(): UserInfo = UserInfo(
	id = id,
	username = username,
	email = email,
	avatarUrl = avatarUrl
)

fun ClosetItemDto.toUi(): ClosetItemData = ClosetItemData(
	id        = id,
	title     = title,
	imageUrl  = imageUrl,
	category  = category,
	season    = season,
	occasion  = occasion,
	color     = color,
	material  = material,
	dateAdded = dateAdded
)

fun OutfitItemDto.toUi(): OutfitItemData = OutfitItemData(
	id        = id,
	title     = title,
	imageUrl  = imageUrl,
	season    = season,
	occasion  = occasion,
	color     = color,
	dateAdded = dateAdded
)
