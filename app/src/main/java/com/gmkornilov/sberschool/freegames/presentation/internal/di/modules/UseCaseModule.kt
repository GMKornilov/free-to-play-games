package com.gmkornilov.sberschool.freegames.presentation.internal.di.modules

import com.gmkornilov.sberschool.freegames.domain.entity.gameinfo.GameInfo
import com.gmkornilov.sberschool.freegames.domain.entity.gamepreview.GamePreview
import com.gmkornilov.sberschool.freegames.domain.interactor.SingleUseCase
import com.gmkornilov.sberschool.freegames.domain.interactor.gameinfo.GetGameInfoUseCase
import com.gmkornilov.sberschool.freegames.domain.interactor.gamelist.GetAllGamePreviewsUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface UseCaseModule {
    @Binds
    fun provideGetGameInfoUseCase(getGameInfoUseCase: GetGameInfoUseCase): SingleUseCase<GameInfo, Int>

    @Binds
    fun provideGetAllGamePreviewsUseCase(getAllGamePreviewsUseCase: GetAllGamePreviewsUseCase): SingleUseCase<List<GamePreview>, Unit>
}