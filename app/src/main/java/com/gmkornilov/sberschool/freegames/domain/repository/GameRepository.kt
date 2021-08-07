package com.gmkornilov.sberschool.freegames.domain.repository

import com.gmkornilov.sberschool.freegames.domain.entity.gameinfo.GameInfo
import com.gmkornilov.sberschool.freegames.domain.entity.gamepreview.GamePreview

/**
 * Repository for getting information about free-to-play games
 */
interface GameRepository {
    /**
     * Loads previews of all games
     */
    fun getAllGamePreviews(): List<GamePreview>

    /**
     * Gets information about specific game
     */
    fun getGameInfo(gameId: Long): GameInfo

    // TODO: add filtering game previews
}