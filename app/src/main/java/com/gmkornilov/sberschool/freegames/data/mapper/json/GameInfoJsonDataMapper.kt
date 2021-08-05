package com.gmkornilov.sberschool.freegames.data.mapper.json

import com.gmkornilov.sberschool.freegames.domain.entity.gameinfo.GameInfo
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.addAdapter
import java.util.*
import javax.inject.Inject

class GameInfoJsonDataMapper @Inject constructor() : JsonDataMapper<GameInfo> {
    private val moshi = Moshi.Builder()
        .add(Date::class.java, Rfc3339DateJsonAdapter())
        .build()

    private val adapter: JsonAdapter<GameInfo> = moshi.adapter(GameInfo::class.java)

    private val type = Types.newParameterizedType(List::class.java, GameInfo::class.java)
    private val listAdapter: JsonAdapter<List<GameInfo>> = moshi.adapter(type)

    override fun decode(json: String): GameInfo {
        return adapter.fromJson(json)!!
    }

    override fun decodeCollection(json: String): List<GameInfo> {
        return listAdapter.fromJson(json)!!
    }

    override fun encode(item: GameInfo): String {
        return adapter.toJson(item)
    }

    override fun encodeCollection(items: List<GameInfo>): String {
        return listAdapter.toJson(items)
    }
}