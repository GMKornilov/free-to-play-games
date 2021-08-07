package com.gmkornilov.sberschool.freegames.domain.interactor

import io.reactivex.rxjava3.core.Single

/**
 * Interactor for use case(from Clean Architecture),
 * which returns single value
 */
interface SingleUseCase<Type, Params> {
    /**
     * Builds Single for processing given use-case
     *
     * @param params use-case parameter
     *
     * @return Single which contains either failure from executing use-case
     * or result from use-case execution
     */
    fun buildSingle(params: Params): Single<Type>
}