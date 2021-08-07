package com.gmkornilov.sberschool.freegames.domain.navigation

import android.content.Intent
import com.gmkornilov.sberschool.freegames.domain.entity.navigation.GameInfoNavigationInfo

/**
 * Interface for navigating between screens
 */
interface Navigator {
    /**
     * Constructs intent for game information activity
     */
    fun showGameInfo(gameInfoNavigationInfo: GameInfoNavigationInfo): Intent

    // TODO: add show filters
}