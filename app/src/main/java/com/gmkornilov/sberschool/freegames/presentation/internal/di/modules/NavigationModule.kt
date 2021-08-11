package com.gmkornilov.sberschool.freegames.presentation.internal.di.modules

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {
    private val cicerone = Cicerone.create()

    @Provides
    fun provideRouter(): Router = cicerone.router

    @Provides
    fun provideNavigationHolder(): NavigatorHolder = cicerone.getNavigatorHolder()
}