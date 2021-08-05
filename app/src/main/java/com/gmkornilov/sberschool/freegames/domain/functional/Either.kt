package com.gmkornilov.sberschool.freegames.domain.functional

/**
 * Class containing one of two defined types
 */
sealed class Either<out L, out R> {
    data class Left<out L>(val value: L): Either<L, Nothing>()

    data class Right<out R>(val value: R): Either<Nothing, R>()
}
