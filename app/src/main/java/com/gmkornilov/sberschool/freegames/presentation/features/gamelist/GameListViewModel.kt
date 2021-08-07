package com.gmkornilov.sberschool.freegames.presentation.features.gamelist

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gmkornilov.sberschool.freegames.domain.entity.gamepreview.GamePreview
import com.gmkornilov.sberschool.freegames.domain.entity.navigation.GameInfoNavigationInfo
import com.gmkornilov.sberschool.freegames.domain.exception.Failure
import com.gmkornilov.sberschool.freegames.domain.functional.Either
import com.gmkornilov.sberschool.freegames.domain.interactor.SingleUseCase
import com.gmkornilov.sberschool.freegames.domain.interactor.gamelist.ShowGameInfoUseCase
import com.gmkornilov.sberschool.freegames.domain.rx.SchedulersProvider
import com.gmkornilov.sberschool.freegames.presentation.features.gamelist.adapter.GamePreviewClicked
import com.gmkornilov.sberschool.freegames.presentation.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject

@HiltViewModel
class GameListViewModel @Inject constructor(
    private val getAllGamePreviewsUseCase: SingleUseCase<List<GamePreview>, Unit>,
    private val showGameInfoUseCase: ShowGameInfoUseCase,
    private val schedulersProvider: SchedulersProvider,
) : ViewModel(), GamePreviewClicked {
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
            .subscribe { either -> processGamePreviewsEither(either) }
    }

    override fun onGamePreviewClicked(gameInfoNavigationInfo: GameInfoNavigationInfo) {
        showGameInfo(gameInfoNavigationInfo)
    }


    private fun showGameInfo(gameInfoNavigationInfo: GameInfoNavigationInfo) {
        showGameInfoDisposable?.dispose()

        showGameInfoDisposable = showGameInfoUseCase.buildSingle(gameInfoNavigationInfo)
            .subscribeOn(schedulersProvider.main())
            .subscribe { either ->
                if (either is Either.Right) {
                    startActivityEvent.postValue(Pair(either.value, gameInfoNavigationInfo))
                }
            }
    }

    private fun processGamePreviewsEither(either: Either<Failure, List<GamePreview>>) {
        when (either) {
            is Either.Left -> processFailure(either.value)
            is Either.Right -> _gamePreviews.value = either.value
        }
    }

    private fun processFailure(failure: Failure) {
        isFailure.value = true
        // we don't have any feature-specific failures in this call, so we don't process them
        when (failure) {
            is Failure.ExceptionFailure -> {
                Log.d(TAG, "Unknown exception:", failure.t)
                _exception.value = true
            }
            Failure.NetworkConnection -> _networkError.value = true
            Failure.ServerError -> _serverError.value = true
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