package com.gmkornilov.sberschool.freegames.domain.navigation

import com.gmkornilov.sberschool.freegames.domain.entity.gamepreview.GamePreview

/**
 * Interface for navigating between screens
 */
interface ApplicationNavigator {
    /**
     * Constructs intent for game information activity
     */
    fun showGameInfo(gamePreview: GamePreview)

    // TODO: add show filters
}