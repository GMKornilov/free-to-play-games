package com.gmkornilov.sberschool.freegames.presentation.internal.di.modules

import com.gmkornilov.sberschool.freegames.domain.navigation.Navigator
import com.gmkornilov.sberschool.freegames.presentation.navigation.NavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface NavigatorModule {
    @Binds
    fun provideNavigator(navigatorImpl: NavigatorImpl): Navigator
}