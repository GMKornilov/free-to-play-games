package com.gmkornilov.sberschool.freegames.domain.interactor.gamelist

import com.gmkornilov.sberschool.freegames.domain.entity.gamepreview.GamePreview
import com.gmkornilov.sberschool.freegames.domain.interactor.CompletableUseCase
import com.gmkornilov.sberschool.freegames.domain.navigation.ApplicationNavigator
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

/**
 * Use case of showing information about specific game.
 */
class ShowGameInfoUseCase @Inject constructor(
    private val applicationNavigator: ApplicationNavigator,
) : CompletableUseCase<@JvmSuppressWildcards GamePreview> {
    override fun buildCompletable(params: GamePreview): Completable {
        return Completable.fromAction {
            applicationNavigator.showGameInfo(params)
        }
    }
}