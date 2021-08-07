package com.gmkornilov.sberschool.freegames.data.reposiotry

import com.gmkornilov.sberschool.freegames.data.mapper.json.JsonDataMapper
import com.gmkornilov.sberschool.freegames.domain.entity.gameinfo.GameInfo
import com.gmkornilov.sberschool.freegames.domain.entity.gamepreview.GamePreview
import com.gmkornilov.sberschool.freegames.domain.exception.GameNotFound
import com.gmkornilov.sberschool.freegames.domain.exception.NetworkConnectionException
import com.gmkornilov.sberschool.freegames.domain.exception.ServerException
import com.gmkornilov.sberschool.freegames.domain.repository.GameRepository
import io.reactivex.rxjava3.core.Single
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

    override fun getAllGamePreviews(): Single<List<GamePreview>> {
        return Single.create { emitter ->
            val url = "$BASE_URL/games"
            val request = Request.Builder()
                .url(url)
                .get()
                .build()
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val body = response.body!!.string()
                    val gamePreviews = gamePreviewJsonDataMapper.decodeCollection(body)
                    emitter.onSuccess(gamePreviews)
                } else {
                    emitter.onError(ServerException())
                }
            }
        }
    }

    override fun getGameInfo(gameId: Long): Single<GameInfo> {
        return Single.create { emitter ->
            emitter.onError(Exception())
            val url = "$BASE_URL/game?id=$gameId"
            val request = Request.Builder()
                .url(url)
                .get()
                .build()
            try {
                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        val body = response.body!!.string()
                        val gamePreview = gameInfoJsonDataMapper.decode(body)
                        emitter.onSuccess(gamePreview)
                    } else if (response.code == NOT_FOUND_STATUS) {
                        emitter.onError(GameNotFound())
                    } else {
                        emitter.onError(ServerException())
                    }
                }
            }
            catch (ex: IOException) {
                emitter.onError(NetworkConnectionException())
            }
        }
    }

    companion object {
        private const val CALL_TIMEOUT = 3L

        private const val BASE_URL = "https://www.freetogame.com/api"

        private const val NOT_FOUND_STATUS = 404
    }
}