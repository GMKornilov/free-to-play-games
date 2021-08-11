package com.gmkornilov.sberschool.freegames.presentation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.gmkornilov.sberschool.freegames.domain.entity.navigation.GameInfoNavigationInfo
import com.gmkornilov.sberschool.freegames.presentation.features.gameinfo.GameInfoFragment
import com.gmkornilov.sberschool.freegames.presentation.features.gamelist.GameListFragment

object Screens {
    fun GamePreviewsList() = FragmentScreen {
        GameListFragment.newInstance()
    }

    fun GameInfo(gameInfoNavigationInfo: GameInfoNavigationInfo) = FragmentScreen {
        GameInfoFragment.newFragment(gameInfoNavigationInfo)
    }
}