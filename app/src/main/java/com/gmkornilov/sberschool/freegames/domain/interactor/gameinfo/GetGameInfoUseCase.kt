package com.gmkornilov.sberschool.freegames.domain.interactor.gameinfo

import com.gmkornilov.sberschool.freegames.domain.entity.gameinfo.GameInfo
import com.gmkornilov.sberschool.freegames.domain.interactor.SingleUseCase
import com.gmkornilov.sberschool.freegames.domain.repository.GameRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

/**
 * Interactor for use case of getting information about specific game by its id.
 */
class GetGameInfoUseCase @Inject constructor(
    private val repository: GameRepository,
    // Annotation @JvmSupressWildcards is added in order to prevent build fails from Dagger
    // (https://stackoverflow.com/questions/60320337/dagger2-binds-methods-parameter-type-must-be-assignable-to-the-return-type-wit)
) : SingleUseCase<@JvmSuppressWildcards GameInfo, Long> {
    override fun buildSingle(params: Long): Single<GameInfo> {
        return Single.fromCallable {
            repository.getGameInfo(params)
        }
    }
}