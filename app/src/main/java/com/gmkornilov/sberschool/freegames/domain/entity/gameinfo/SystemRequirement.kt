package com.gmkornilov.sberschool.freegames.domain.entity.gameinfo

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SystemRequirement(
    val os: String? = null,
    val processor: String? = null,
    val memory: String? = null,
    val graphics: String? = null,
    val storage: String? = null,
)
