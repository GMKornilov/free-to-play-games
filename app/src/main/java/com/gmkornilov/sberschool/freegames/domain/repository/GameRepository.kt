package com.gmkornilov.sberschool.freegames.domain.repository

import com.gmkornilov.sberschool.freegames.domain.entity.gameinfo.GameInfo
import com.gmkornilov.sberschool.freegames.domain.entity.gamepreview.GamePreview
import io.reactivex.rxjava3.core.Single

interface GameRepository {
    fun getAllGamePreviews(): Single<List<GamePreview>>

    fun getGameInfo(gameId: Int): Single<GameInfo>

    // TODO: add filtering game previews
}