package com.gmkornilov.sberschool.freegames.domain.interactor.gameinfo

import com.gmkornilov.sberschool.freegames.domain.entity.gameinfo.GameInfo
import com.gmkornilov.sberschool.freegames.domain.exception.Failure
import com.gmkornilov.sberschool.freegames.domain.exception.GameNotFound
import com.gmkornilov.sberschool.freegames.domain.exception.ServerException
import com.gmkornilov.sberschool.freegames.domain.functional.Either
import com.gmkornilov.sberschool.freegames.domain.interactor.SingleUseCase
import com.gmkornilov.sberschool.freegames.domain.repository.GameRepository
import com.gmkornilov.sberschool.freegames.domain.rx.SchedulersProvider
import io.reactivex.rxjava3.core.Single
import java.io.IOException
import javax.inject.Inject

/**
 * Interactor for use case of getting information about specific game by its id.
 */
class GetGameInfoUseCase @Inject constructor(
    private val repository: GameRepository,
    private val schedulersProvider: SchedulersProvider,
    // Annotation @JvmSupressWildcards is added in order to prevent build fails from Dagger
    // (https://stackoverflow.com/questions/60320337/dagger2-binds-methods-parameter-type-must-be-assignable-to-the-return-type-wit)
) : SingleUseCase<@JvmSuppressWildcards GameInfo, Int> {
    override fun buildSingle(params: Int): Single<Either<Failure, GameInfo>> {
        return repository
            .getGameInfo(params)
            .map<Either<Failure, GameInfo>> {
                Either.Right(it)
            }
            .onErrorReturn {
                Either.Left(
                    when (it) {
                        is ServerException -> Failure.ServerError
                        is GameNotFound -> Failure.GameNotFound
                        is IOException -> Failure.NetworkConnection
                        else -> Failure.ExceptionFailure(it)
                    }
                )
            }.subscribeOn(schedulersProvider.io())
    }
}