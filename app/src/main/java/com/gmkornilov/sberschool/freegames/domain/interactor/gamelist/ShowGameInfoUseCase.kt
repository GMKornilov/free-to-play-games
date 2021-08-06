package com.gmkornilov.sberschool.freegames.domain.interactor.gamelist

import android.content.Intent
import com.gmkornilov.sberschool.freegames.domain.entity.gameinfo.GameInfoNavigationInfo
import com.gmkornilov.sberschool.freegames.domain.entity.gamepreview.GamePreview
import com.gmkornilov.sberschool.freegames.domain.exception.Failure
import com.gmkornilov.sberschool.freegames.domain.functional.Either
import com.gmkornilov.sberschool.freegames.domain.interactor.CompletableUseCase
import com.gmkornilov.sberschool.freegames.domain.interactor.SingleUseCase
import com.gmkornilov.sberschool.freegames.domain.navigation.Navigator
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ShowGameInfoUseCase @Inject constructor(
    private val navigator: Navigator,
) : CompletableUseCase<GameInfoNavigationInfo> {
    override fun buildCompletable(params: GameInfoNavigationInfo): Completable {

    }
}