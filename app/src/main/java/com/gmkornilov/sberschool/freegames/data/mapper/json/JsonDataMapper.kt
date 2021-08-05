package com.gmkornilov.sberschool.freegames.data.mapper.json

/**
 * Mapper for transforming type from/to JSON format.
 */
interface JsonDataMapper<T> {
    /**
     * Decodes JSON representation of object
     */
    fun decode(json: String): T

    /**
     * Decodes JSON representation of list of objects
     */
    fun decodeCollection(json: String): List<T>

    /**
     * Encodes objects to JSON format
     */
    fun encode(item: T): String

    /**
     * Encode list of objects to JSON format
     */
    fun encodeCollection(items: List<T>): String
}