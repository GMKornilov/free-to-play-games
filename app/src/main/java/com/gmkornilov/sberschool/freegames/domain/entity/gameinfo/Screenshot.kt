package com.gmkornilov.sberschool.freegames.domain.entity.gameinfo

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Screenshot(
    val id: Long,
    @Json(name = "image") val imageUrl: String,
)
