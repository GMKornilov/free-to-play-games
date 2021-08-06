package com.gmkornilov.sberschool.freegames.presentation.features.gameinfo

import android.util.Log
import androidx.lifecycle.*
import com.gmkornilov.sberschool.freegames.domain.entity.gameinfo.GameInfo
import com.gmkornilov.sberschool.freegames.domain.entity.gamepreview.GamePreview
import com.gmkornilov.sberschool.freegames.domain.exception.Failure
import com.gmkornilov.sberschool.freegames.domain.functional.Either
import com.gmkornilov.sberschool.freegames.domain.interactor.SingleUseCase
import com.gmkornilov.sberschool.freegames.domain.rx.SchedulersProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.assisted.AssistedFactory
import io.reactivex.rxjava3.disposables.Disposable

class GameInfoViewModel @AssistedInject constructor(
    private val getGameInfoUseCase: SingleUseCase<GameInfo, Int>,
    private val schedulersProvider: SchedulersProvider,
    @Assisted private val gamePreviewInfo: GamePreview,
) : ViewModel() {
    private val _serverError: MutableLiveData<Boolean> = MutableLiveData(false)
    val serverError: LiveData<Boolean> = _serverError

    private val _networkError: MutableLiveData<Boolean> = MutableLiveData(false)
    val networkError: LiveData<Boolean> = _networkError

    private val _exception: MutableLiveData<Boolean> = MutableLiveData(false)
    val exception: LiveData<Boolean> = _exception

    private var getGameInfoDisposable: Disposable? = null

    private val _gameInfo: MutableLiveData<GameInfo> = MutableLiveData()
    val gameInfo: LiveData<GameInfo> = _gameInfo

    val gamePreview:LiveData<GamePreview> = MutableLiveData<GamePreview>().apply {
        value = gamePreviewInfo
    }

    private val _loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val isFailure: MutableLiveData<Boolean> = MutableLiveData(false)

    private val _successfullyLoaded: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()
    val successfullyLoaded: LiveData<Boolean> = _successfullyLoaded

    init {
        _successfullyLoaded.addSource(loading) {
            val loadingValue = loading.value ?: false
            val isFailureValue = isFailure.value ?: false
            _successfullyLoaded.value = !(loadingValue || isFailureValue)
        }
        _successfullyLoaded.addSource(isFailure) {
            val loadingValue = loading.value ?: false
            val isFailureValue = isFailure.value ?: false
            _successfullyLoaded.value = !(loadingValue || isFailureValue)
        }
    }

    private fun getGameInfo() {
        getGameInfoDisposable?.dispose()

        getGameInfoDisposable = getGameInfoUseCase.buildSingle(gamePreviewInfo.id)
            .doOnSubscribe {
                _loading.postValue(true)
                isFailure.postValue(false)
            }
            .doAfterTerminate {
                _loading.postValue(false)
            }
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.main())
            .subscribe { either -> processGameInfoEither(either) }
    }

    private fun processGameInfoEither(either: Either<Failure, GameInfo>) {
        when (either) {
            is Either.Left -> processFailure(either.value)
            is Either.Right -> _gameInfo.value = either.value
        }
    }

    private fun processFailure(failure: Failure) {
        isFailure.value = true
        when(failure) {
            is Failure.ExceptionFailure -> {
                Log.d(TAG, "Unknown exception:", failure.t)
                _exception.value = true
            }
            is Failure.NetworkConnection -> _networkError.value = true
            is Failure.ServerError -> _serverError.value = true
        }
    }

    override fun onCleared() {
        super.onCleared()

        getGameInfoDisposable?.dispose()
    }

    /**
     * Factory interface for assisted injection
     */
    @AssistedFactory
    interface ViewModelAssistedFactory {
        fun create(gamePreview: GamePreview): GameInfoViewModel
    }

    companion object {
        private const val TAG = "GameInfoViewModel"

        fun provideFactory(
            assistedFactory: ViewModelAssistedFactory,
            gamePreview: GamePreview,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(gamePreview) as T
            }

        }
    }
}