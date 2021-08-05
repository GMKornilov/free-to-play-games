package com.gmkornilov.sberschool.freegames.data.mapper.json

import com.gmkornilov.sberschool.freegames.domain.entity.gamepreview.GamePreview
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import java.util.*
import javax.inject.Inject

class GamePreviewJsonDataMapper @Inject constructor() : JsonDataMapper<GamePreview> {
    private val moshi = Moshi.Builder()
        .add(Date::class.java, Rfc3339DateJsonAdapter())
        .build()

    private val adapter: JsonAdapter<GamePreview> = moshi.adapter(GamePreview::class.java)

    private val type = Types.newParameterizedType(List::class.java, GamePreview::class.java)
    private val listAdapter: JsonAdapter<List<GamePreview>> = moshi.adapter(type)

    override fun decode(json: String): GamePreview {
        return adapter.fromJson(json)!!
    }

    override fun decodeCollection(json: String): List<GamePreview> {
        return listAdapter.fromJson(json)!!
    }

    override fun encode(item: GamePreview): String {
        return adapter.toJson(item)
    }

    override fun encodeCollection(items: List<GamePreview>): String {
        return listAdapter.toJson(items)
    }
}