package com.gmkornilov.sberschool.freegames.domain.rx

import io.reactivex.rxjava3.core.Scheduler

/**
 * Provider of RxJava Schedulers
 */
interface SchedulersProvider {
    /**
     * Provides Scheduler for io operations
     */
    fun io(): Scheduler

    /**
     * Provides Scheduler for main thread operations
     */
    fun main(): Scheduler
}