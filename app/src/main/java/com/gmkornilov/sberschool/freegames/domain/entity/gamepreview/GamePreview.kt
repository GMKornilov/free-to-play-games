package com.gmkornilov.sberschool.freegames.domain.entity.gamepreview

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class GamePreview(
    val id: Long,
    val title: String,
    @Json(name = "thumbnail") val thumbnailUrl: String,
    @Json(name = "short_description") val description: String,
    @Json(name = "game_url") val gameUrl: String,
    val genre: String,
    val platform: String,
    val developer: String,
    @Json(name = "release_date") val releaseDate: Date,
    @Json(name = "freetogame_profile_url") val freeToGameProfileUrl: String,
)