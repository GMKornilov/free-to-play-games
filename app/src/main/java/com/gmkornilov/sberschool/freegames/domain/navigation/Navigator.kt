package com.gmkornilov.sberschool.freegames.domain.navigation

import com.gmkornilov.sberschool.freegames.domain.entity.gameinfo.GameInfoNavigationInfo
import com.gmkornilov.sberschool.freegames.domain.entity.gamepreview.GamePreview

interface Navigator {
    fun showGameInfo(gameInfoNavigationInfo: GameInfoNavigationInfo)

    // TODO: add show filters
}