package com.gmkornilov.sberschool.freegames.presentation.navigation

import android.content.Context
import android.content.Intent
import com.gmkornilov.sberschool.freegames.domain.entity.navigation.GameInfoNavigationInfo
import com.gmkornilov.sberschool.freegames.domain.navigation.Navigator
import com.gmkornilov.sberschool.freegames.presentation.features.gameinfo.GameInfoActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigatorImpl @Inject constructor(
    @ApplicationContext
    private val context: Context,
) : Navigator {
    override fun showGameInfo(gameInfoNavigationInfo: GameInfoNavigationInfo): Intent {
        return GameInfoActivity.newIntent(context, gameInfoNavigationInfo)
    }
}