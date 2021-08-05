package com.gmkornilov.sberschool.freegames.domain.exception

/**
 * Base class for handling exceptions/failures
 */
sealed class Failure {
    /**
     * Use this class for network connection failures
     */
    object NetworkConnection : Failure()

    /**
     * Use this class, when server returns 400-499 or 500+
     * http codes from http requests
     */
    object ServerError : Failure()

    /**
     * Use this class for unexpected exceptions failures
     */
    data class ExceptionFailure(val t: Throwable) : Failure()

    /**
     * Extend this class for feature specific failures
     */
    abstract class FeatureFailure : Failure()

    /**
     * Use this class when request for getting game ifo returns 404 status code
     */
    object GameNotFound : FeatureFailure()
}