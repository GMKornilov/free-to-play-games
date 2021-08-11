package com.gmkornilov.sberschool.freegames.domain.navigation

import com.gmkornilov.sberschool.freegames.domain.entity.navigation.GameInfoNavigationInfo

/**
 * Interface for navigating between screens
 */
interface ApplicationNavigator {
    /**
     * Constructs intent for game information activity
     */
    fun showGameInfo(gameInfoNavigationInfo: GameInfoNavigationInfo)

    // TODO: add show filters
}