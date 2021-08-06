package com.gmkornilov.sberschool.freegames.domain.entity.gameinfo

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class GameInfo(
    val id: Long,
    val title: String,
    @Json(name = "thumbnail") val thumbnailUrl: String,
    val status: String,
    @Json(name = "short_description") val shortDescription: String,
    val description: String,
    @Json(name = "game_url") val gameUrl: String,
    val genre: String,
    val platform: String,
    val publisher: String,
    val developer: String,
    @Json(name = "release_date") val releaseDate: Date,
    @Json(name = "freetogame_profile_url") val freeToGameProfileUrl: String,
    val screenshots: List<Screenshot>,
    @Json(name = "minimum_system_requirements") val minimumSystemRequirements: SystemRequirement? = null,
)