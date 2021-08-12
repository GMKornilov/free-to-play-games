package com.gmkornilov.sberschool.freegames.presentation.internal.di.modules

import com.gmkornilov.sberschool.freegames.domain.navigation.ApplicationNavigator
import com.gmkornilov.sberschool.freegames.presentation.navigation.ApplicationNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ApplicationNavigatorModule {
    @Binds
    fun provideApplicationNavigator(navigatorImpl: ApplicationNavigatorImpl): ApplicationNavigator
}