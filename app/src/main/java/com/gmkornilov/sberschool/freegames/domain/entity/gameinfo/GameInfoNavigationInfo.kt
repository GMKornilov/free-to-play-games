package com.gmkornilov.sberschool.freegames.domain.entity.gameinfo

import com.gmkornilov.sberschool.freegames.domain.entity.gamepreview.GamePreview

data class GameInfoNavigationInfo(
    val gamePreview: GamePreview,
    val sharedTitleName: String,
    val sharedThumbnailName: String
)