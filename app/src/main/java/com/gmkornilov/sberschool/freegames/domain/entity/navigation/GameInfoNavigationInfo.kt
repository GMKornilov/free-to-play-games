package com.gmkornilov.sberschool.freegames.domain.entity.navigation

import android.widget.ImageView
import android.widget.TextView
import com.gmkornilov.sberschool.freegames.domain.entity.gamepreview.GamePreview

data class GameInfoNavigationInfo(
    val gamePreview: GamePreview,

    val sharedTitleName: String,
    val sharedThumbnailName: String,

    val sharedTitle: TextView,
    val sharedImageView: ImageView,
)