package com.gmkornilov.sberschool.freegames.domain.interactor.gamelist

import com.gmkornilov.sberschool.freegames.domain.entity.gamepreview.GamePreview
import com.gmkornilov.sberschool.freegames.domain.exception.Failure
import com.gmkornilov.sberschool.freegames.domain.exception.ServerException
import com.gmkornilov.sberschool.freegames.domain.functional.Either
import com.gmkornilov.sberschool.freegames.domain.interactor.SingleUseCase
import com.gmkornilov.sberschool.freegames.domain.repository.GameRepository
import com.gmkornilov.sberschool.freegames.domain.rx.SchedulersProvider
import io.reactivex.rxjava3.core.Single
import java.io.IOException
import javax.inject.Inject

/**
 * Interactor for getting preview information about all free-to-play games
 */
class GetAllGamePreviewsUseCase @Inject constructor(
    private val repository: GameRepository,
    private val schedulersProvider: SchedulersProvider,
    // Annotation @JvmSupressWildcards is added in order to prevent build fails from Dagger
    // (https://stackoverflow.com/questions/60320337/dagger2-binds-methods-parameter-type-must-be-assignable-to-the-return-type-wit)
) : SingleUseCase<@JvmSuppressWildcards List<GamePreview>, Unit> {
    override fun buildSingle(params: Unit): Single<Either<Failure, List<GamePreview>>> {
        return repository
            .getAllGamePreviews()
            .map<Either<Failure, List<GamePreview>>> {
                Either.Right(it)
            }
            .onErrorReturn {
                Either.Left(
                    when (it) {
                        is ServerException -> Failure.ServerError
                        is IOException -> Failure.NetworkConnection
                        else -> Failure.ExceptionFailure(it)
                    }
                )
            }
    }
}