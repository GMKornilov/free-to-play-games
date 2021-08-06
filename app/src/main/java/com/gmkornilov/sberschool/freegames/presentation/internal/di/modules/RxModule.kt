package com.gmkornilov.sberschool.freegames.presentation.internal.di.modules

import com.gmkornilov.sberschool.freegames.domain.rx.SchedulersProvider
import com.gmkornilov.sberschool.freegames.domain.rx.SchedulersProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class, ActivityComponent::class)
interface RxModule {
    @Binds
    fun provideSchedulersProvider(schedulersProvider: SchedulersProviderImpl): SchedulersProvider
}