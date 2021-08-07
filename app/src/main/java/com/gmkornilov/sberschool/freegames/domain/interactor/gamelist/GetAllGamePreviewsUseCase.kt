package com.gmkornilov.sberschool.freegames.domain.interactor.gamelist

import com.gmkornilov.sberschool.freegames.domain.entity.gamepreview.GamePreview
import com.gmkornilov.sberschool.freegames.domain.interactor.SingleUseCase
import com.gmkornilov.sberschool.freegames.domain.repository.GameRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

/**
 * Interactor for getting preview information about all free-to-play games
 */
class GetAllGamePreviewsUseCase @Inject constructor(
    private val repository: GameRepository,
    // Annotation @JvmSupressWildcards is added in order to prevent build fails from Dagger
    // (https://stackoverflow.com/questions/60320337/dagger2-binds-methods-parameter-type-must-be-assignable-to-the-return-type-wit)
) : SingleUseCase<@JvmSuppressWildcards List<GamePreview>, Unit> {
    override fun buildSingle(params: Unit): Single<List<GamePreview>> {
        return Single.fromCallable {
            repository.getAllGamePreviews()
        }
    }
}