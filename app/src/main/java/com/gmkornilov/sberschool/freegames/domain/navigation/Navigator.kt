package com.gmkornilov.sberschool.freegames.domain.navigation

import android.content.Intent
import com.gmkornilov.sberschool.freegames.domain.entity.navigation.GameInfoNavigationInfo

interface Navigator {
    fun showGameInfo(gameInfoNavigationInfo: GameInfoNavigationInfo): Intent

    // TODO: add show filters
}