package com.gmkornilov.sberschool.freegames.presentation.features.gameinfo

import android.util.Log
import androidx.lifecycle.*
import com.gmkornilov.sberschool.freegames.domain.entity.gameinfo.GameInfo
import com.gmkornilov.sberschool.freegames.domain.entity.gameinfo.Screenshot
import com.gmkornilov.sberschool.freegames.domain.entity.gameinfo.SystemRequirement
import com.gmkornilov.sberschool.freegames.domain.entity.gamepreview.GamePreview
import com.gmkornilov.sberschool.freegames.domain.exception.GameNotFound
import com.gmkornilov.sberschool.freegames.domain.exception.NetworkConnectionException
import com.gmkornilov.sberschool.freegames.domain.exception.ServerException
import com.gmkornilov.sberschool.freegames.domain.interactor.SingleUseCase
import com.gmkornilov.sberschool.freegames.domain.rx.SchedulersProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.reactivex.rxjava3.disposables.Disposable

// This class is not annotated with @HiltViewModel annotation because
// Hilt does not support assisted injection for class with this annotation
// (and it sucks)
class GameInfoViewModel @AssistedInject constructor(
    private val getGameInfoUseCase: SingleUseCase<GameInfo, Long>,
    private val schedulersProvider: SchedulersProvider,
    @Assisted private val gamePreviewInfo: GamePreview,
) : ViewModel() {
    private val _serverError: MutableLiveData<Boolean> = MutableLiveData()
    val serverError: LiveData<Boolean> = _serverError

    private val _networkError: MutableLiveData<Boolean> = MutableLiveData()
    val networkError: LiveData<Boolean> = _networkError

    private val _notFoundError: MutableLiveData<Boolean> = MutableLiveData()
    val notFoundError: MutableLiveData<Boolean> = _notFoundError

    private val _exception: MutableLiveData<Boolean> = MutableLiveData()
    val exception: LiveData<Boolean> = _exception

    private var getGameInfoDisposable: Disposable? = null

    private val _gameInfo: MutableLiveData<GameInfo> = MutableLiveData()
    val gameInfo: LiveData<GameInfo> = _gameInfo

    private val _description: MediatorLiveData<String> = MediatorLiveData()
    val description: LiveData<String> = _description

    private val _developer: MediatorLiveData<String> = MediatorLiveData()
    val developer: LiveData<String> = _developer

    private val _requirements: MediatorLiveData<SystemRequirement> = MediatorLiveData()
    val requirements: LiveData<SystemRequirement> = _requirements

    private val _screenshots: MediatorLiveData<List<Screenshot>> = MediatorLiveData()
    val screenshots: LiveData<List<Screenshot>> = _screenshots

    val gamePreview: LiveData<GamePreview> = MutableLiveData<GamePreview>().apply {
        value = gamePreviewInfo
    }

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> = _loading

    private val isFailure: MutableLiveData<Boolean> = MutableLiveData(false)

    private val _successfullyLoaded: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()
    val successfullyLoaded: LiveData<Boolean> = _successfullyLoaded

    init {
        _successfullyLoaded.addSource(loading) {
            val loadingValue = loading.value
            val isFailureValue = isFailure.value
            _successfullyLoaded.value = if (loadingValue != null && isFailureValue != null) {
                !(loadingValue || isFailureValue)
            } else {
                false
            }
        }
        _successfullyLoaded.addSource(isFailure) {
            val loadingValue = loading.value
            val isFailureValue = isFailure.value
            _successfullyLoaded.value = if (loadingValue != null && isFailureValue != null) {
                !(loadingValue || isFailureValue)
            } else {
                false
            }
        }

        _description.addSource(_gameInfo) {
            _description.value = it.description
        }

        _developer.addSource(_gameInfo) {
            _developer.value = it.developer
        }

        _requirements.addSource(_gameInfo) {
            val requirements = it.minimumSystemRequirements
            if (requirements?.graphics != null
                && requirements.memory != null
                && requirements.processor != null
                && requirements.os != null
                && requirements.storage != null
            ) {
                _requirements.value = requirements
            }
        }

        _screenshots.addSource(_gameInfo) {
            _screenshots.value = it.screenshots
        }

        getGameInfo()
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
            .subscribe { gameInfo, throwable ->
                if (throwable != null) {
                    processException(throwable)
                } else {
                    _gameInfo.value = gameInfo
                }
            }
    }

    private fun processException(throwable: Throwable) {
        isFailure.value = true
        when (throwable) {
            is GameNotFound -> _notFoundError.value = true
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