package com.gmkornilov.sberschool.freegames.presentation.features.gamelist.adapter

import com.gmkornilov.sberschool.freegames.domain.entity.navigation.GameInfoNavigationInfo

interface GamePreviewClicked {
    fun onGamePreviewClicked(gameInfoNavigationInfo: GameInfoNavigationInfo)
}