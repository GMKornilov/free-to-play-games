package com.gmkornilov.sberschool.freegames.presentation.internal.di.modules

import com.gmkornilov.sberschool.freegames.domain.entity.gameinfo.GameInfo
import com.gmkornilov.sberschool.freegames.domain.entity.gamepreview.GamePreview
import com.gmkornilov.sberschool.freegames.domain.interactor.CompletableUseCase
import com.gmkornilov.sberschool.freegames.domain.interactor.SingleUseCase
import com.gmkornilov.sberschool.freegames.domain.interactor.gameinfo.GetGameInfoUseCase
import com.gmkornilov.sberschool.freegames.domain.interactor.gamelist.GetAllGamePreviewsUseCase
import com.gmkornilov.sberschool.freegames.domain.interactor.gamelist.ShowGameInfoUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class, FragmentComponent::class)
interface UseCaseModule {
    @Binds
    fun provideGetGameInfoUseCase(getGameInfoUseCase: GetGameInfoUseCase): SingleUseCase<GameInfo, Long>

    @Binds
    fun provideGetAllGamePreviewsUseCase(getAllGamePreviewsUseCase: GetAllGamePreviewsUseCase): SingleUseCase<List<GamePreview>, Unit>

    @Binds
    fun provideShowGameInfoUseCase(showGameInfoUseCase: ShowGameInfoUseCase): CompletableUseCase<GamePreview>
}