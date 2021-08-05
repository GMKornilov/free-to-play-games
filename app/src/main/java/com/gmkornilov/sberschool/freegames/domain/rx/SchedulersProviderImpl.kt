package com.gmkornilov.sberschool.freegames.domain.rx

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class SchedulersProviderImpl @Inject constructor(): SchedulersProvider {
    override fun io(): Scheduler {
        return Schedulers.io()
    }

    override fun main(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}