package com.gmkornilov.sberschool.freegames.presentation.navigation

import com.github.terrakok.cicerone.Router
import com.gmkornilov.sberschool.freegames.domain.entity.gamepreview.GamePreview
import com.gmkornilov.sberschool.freegames.domain.navigation.ApplicationNavigator
import com.gmkornilov.sberschool.freegames.presentation.Screens
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApplicationNavigatorImpl @Inject constructor(
    private val router: Router
) : ApplicationNavigator {
    override fun showGameInfo(gamePreview: GamePreview) {
        return router.navigateTo(Screens.GameInfo(gamePreview))
    }
}