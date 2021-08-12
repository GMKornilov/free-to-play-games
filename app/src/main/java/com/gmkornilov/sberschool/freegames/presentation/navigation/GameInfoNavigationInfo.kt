package com.gmkornilov.sberschool.freegames.presentation.navigation

import android.widget.ImageView
import com.gmkornilov.sberschool.freegames.domain.entity.gamepreview.GamePreview

data class GameInfoNavigationInfo(
    val gamePreview: GamePreview,
    val sharedThumbnailName: String,
    val sharedImageView: ImageView,
)