package com.gmkornilov.sberschool.freegames.presentation.internal.di.modules

import com.gmkornilov.sberschool.freegames.data.reposiotry.GameRepositoryImpl
import com.gmkornilov.sberschool.freegames.domain.repository.GameRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {
    @Binds
    fun provideGameRepository(gameRepositoryImpl: GameRepositoryImpl): GameRepository
}