package com.gmkornilov.sberschool.freegames.domain.entity.gameinfo

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SystemRequirement(
    val os: String,
    val processor: String,
    val memory: String,
    val graphics: String,
    val storage: String,
)
