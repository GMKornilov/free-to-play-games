package com.gmkornilov.sberschool.freegames.data.reposiotry

import com.gmkornilov.sberschool.freegames.data.mapper.json.JsonDataMapper
import com.gmkornilov.sberschool.freegames.domain.entity.gameinfo.GameInfo
import com.gmkornilov.sberschool.freegames.domain.entity.gamepreview.GamePreview
import com.gmkornilov.sberschool.freegames.domain.exception.GameNotFound
import com.gmkornilov.sberschool.freegames.domain.exception.NetworkConnectionException
import com.gmkornilov.sberschool.freegames.domain.exception.ServerException
import com.gmkornilov.sberschool.freegames.domain.repository.GameRepository
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(
    private val gamePreviewJsonDataMapper: JsonDataMapper<GamePreview>,
    private val gameInfoJsonDataMapper: JsonDataMapper<GameInfo>,
) : GameRepository {
    private val client = OkHttpClient.Builder()
        .callTimeout(CALL_TIMEOUT, TimeUnit.SECONDS)
        .build()

    override fun getAllGamePreviews(): List<GamePreview> {
        val url = "$BASE_URL/games"
        val request = Request.Builder()
            .url(url)
            .get()
            .build()
        client.newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                val body = response.body!!.string()
                return gamePreviewJsonDataMapper.decodeCollection(body)
            } else {
                throw ServerException()
            }
        }

    }

    override fun getGameInfo(gameId: Long): GameInfo {
        val url = "$BASE_URL/game?id=$gameId"
        val request = Request.Builder()
            .url(url)
            .get()
            .build()
        try {
            client.newCall(request).execute().use { response ->
                when {
                    response.isSuccessful -> {
                        val body = response.body!!.string()
                        return gameInfoJsonDataMapper.decode(body)
                    }
                    response.code == NOT_FOUND_STATUS -> {
                        throw GameNotFound()
                    }
                    else -> {
                        throw ServerException()
                    }
                }
            }
        } catch (ex: IOException) {
            throw NetworkConnectionException()
        }
    }

    companion object {
        private const val CALL_TIMEOUT = 3L

        private const val BASE_URL = "https://www.freetogame.com/api"

        private const val NOT_FOUND_STATUS = 404
    }
}