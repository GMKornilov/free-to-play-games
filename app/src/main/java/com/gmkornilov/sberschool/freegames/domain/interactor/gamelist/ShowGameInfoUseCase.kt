package com.gmkornilov.sberschool.freegames.domain.interactor.gamelist

import android.content.Intent
import com.gmkornilov.sberschool.freegames.domain.entity.navigation.GameInfoNavigationInfo
import com.gmkornilov.sberschool.freegames.domain.exception.Failure
import com.gmkornilov.sberschool.freegames.domain.functional.Either
import com.gmkornilov.sberschool.freegames.domain.interactor.CompletableUseCase
import com.gmkornilov.sberschool.freegames.domain.interactor.SingleUseCase
import com.gmkornilov.sberschool.freegames.domain.navigation.Navigator
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

/**
 * Use case of showing information about specific game.
 * Construct intentn for starting ShowGameInfoActivity
 */
class ShowGameInfoUseCase @Inject constructor(
    private val navigator: Navigator,
) : SingleUseCase<@JvmSuppressWildcards Intent, GameInfoNavigationInfo> {
    override fun buildSingle(params: GameInfoNavigationInfo): Single<Either<Failure, Intent>> {
        return Single.fromCallable {
            navigator.showGameInfo(params)
        }.map {
            Either.Right(it)
        }
    }
}