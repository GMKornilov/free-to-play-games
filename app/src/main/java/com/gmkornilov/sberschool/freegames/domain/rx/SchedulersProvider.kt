package com.gmkornilov.sberschool.freegames.domain.rx

import io.reactivex.rxjava3.core.Scheduler

interface SchedulersProvider {
    fun io(): Scheduler

    fun main(): Scheduler
}