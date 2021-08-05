package com.gmkornilov.sberschool.freegames.domain.interactor

import io.reactivex.rxjava3.core.Completable

/**
 * Interactor for use case(from Clean Architecture),
 * which returns no value
 */
interface CompletableUseCase<Params> {
    /**
     * Builds Single for processing given use-case
     *
     * @param params use-case parameter
     *
     * @return Completable in which our use-case executes
     */
    fun buildCompletable(params: Params): Completable
}