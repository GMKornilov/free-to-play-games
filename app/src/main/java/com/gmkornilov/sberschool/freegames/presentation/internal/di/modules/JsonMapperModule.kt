package com.gmkornilov.sberschool.freegames.presentation.internal.di.modules

import com.gmkornilov.sberschool.freegames.data.mapper.json.GameInfoJsonDataMapper
import com.gmkornilov.sberschool.freegames.data.mapper.json.GamePreviewJsonDataMapper
import com.gmkornilov.sberschool.freegames.data.mapper.json.JsonDataMapper
import com.gmkornilov.sberschool.freegames.domain.entity.gameinfo.GameInfo
import com.gmkornilov.sberschool.freegames.domain.entity.gamepreview.GamePreview
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface JsonMapperModule {
    @Binds
    fun provideGameInfoJsonDataMapper(gameInfoJsonDataMapper: GameInfoJsonDataMapper): JsonDataMapper<GameInfo>

    @Binds
    fun provideGamePreviewJsonDataMapper(gamePreviewJsonDataMapper: GamePreviewJsonDataMapper): JsonDataMapper<GamePreview>
}