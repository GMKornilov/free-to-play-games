package com.gmkornilov.sberschool.freegames.presentation.features.gamelist

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gmkornilov.sberschool.freegames.domain.entity.gamepreview.GamePreview
import com.gmkornilov.sberschool.freegames.presentation.navigation.GameInfoNavigationInfo
import com.gmkornilov.sberschool.freegames.domain.exception.NetworkConnectionException
import com.gmkornilov.sberschool.freegames.domain.exception.ServerException
import com.gmkornilov.sberschool.freegames.domain.interactor.CompletableUseCase
import com.gmkornilov.sberschool.freegames.domain.interactor.SingleUseCase
import com.gmkornilov.sberschool.freegames.domain.rx.SchedulersProvider
import com.gmkornilov.sberschool.freegames.presentation.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject

@HiltViewModel
class GameListViewModel @Inject constructor(
    private val getAllGamePreviewsUseCase: SingleUseCase<List<GamePreview>, Unit>,
    private val showGameInfoUseCase: CompletableUseCase<GamePreview>,
    private val schedulersProvider: SchedulersProvider,
) : ViewModel() {
    private val _serverError: MutableLiveData<Boolean> = MutableLiveData(false)
    val serverError: LiveData<Boolean> = _serverError

    private val _networkError: MutableLiveData<Boolean> = MutableLiveData(false)
    val networkError: LiveData<Boolean> = _networkError

    private val _exception: MutableLiveData<Boolean> = MutableLiveData(false)
    val exception: LiveData<Boolean> = _exception

    private var getAllGamePreviewsDisposable: Disposable? = null
    private var showGameInfoDisposable: Disposable? = null

    private val _gamePreviews: MutableLiveData<List<GamePreview>> = MutableLiveData()
    val gamePreviews: LiveData<List<GamePreview>> = _gamePreviews

    private val _loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val isFailure: MutableLiveData<Boolean> = MutableLiveData(false)

    val startActivityEvent: SingleLiveEvent<Pair<Intent, GameInfoNavigationInfo>> =
        SingleLiveEvent()

    init {
        getAllGamePreviews()
    }

    /**
     * Loads previews of all games and stores them in LiveData
     */
    private fun getAllGamePreviews() {
        getAllGamePreviewsDisposable?.dispose()

        getAllGamePreviewsDisposable = getAllGamePreviewsUseCase.buildSingle(Unit)
            .doOnSubscribe {
                _loading.postValue(true)
                isFailure.postValue(false)
            }
            .doAfterTerminate {
                _loading.postValue(false)
            }
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.main())
            .subscribe { gamePreviewList, throwable ->
                if (throwable != null) {
                    processException(throwable)
                } else {
                    _gamePreviews.value = gamePreviewList
                }
            }
    }

    fun gamePreviewClicked(gamePreview: GamePreview) {
        showGameInfo(gamePreview)
    }

    private fun showGameInfo(gameInfoNavigationInfo: GamePreview) {
        showGameInfoDisposable?.dispose()

        showGameInfoDisposable = showGameInfoUseCase.buildCompletable(gameInfoNavigationInfo)
            .subscribeOn(schedulersProvider.main())
            .subscribe()
    }

    private fun processException(throwable: Throwable) {
        isFailure.value = true
        when (throwable) {
            is NetworkConnectionException -> _networkError.value = true
            is ServerException -> _serverError.value = true
            else -> {
                Log.d(TAG, "Unknown exception:", throwable)
                _exception.value = true
            }
        }
    }

    override fun onCleared() {
        super.onCleared()

        getAllGamePreviewsDisposable?.dispose()
    }

    companion object {
        private const val TAG = "GameListViewModel"
    }
}