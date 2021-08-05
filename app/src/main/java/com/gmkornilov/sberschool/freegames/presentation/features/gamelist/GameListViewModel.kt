package com.gmkornilov.sberschool.freegames.presentation.features.gamelist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gmkornilov.sberschool.freegames.domain.entity.gamepreview.GamePreview
import com.gmkornilov.sberschool.freegames.domain.exception.Failure
import com.gmkornilov.sberschool.freegames.domain.functional.Either
import com.gmkornilov.sberschool.freegames.domain.interactor.SingleUseCase
import com.gmkornilov.sberschool.freegames.domain.rx.SchedulersProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class GameListViewModel @Inject constructor(
    private val getAllGamePreviewsUseCase: SingleUseCase<List<GamePreview>, Unit>,
    private val schedulersProvider: SchedulersProvider,
) : ViewModel() {
    private val _serverError: MutableLiveData<Boolean> = MutableLiveData(false)
    val serverError: LiveData<Boolean> = _serverError

    private val _networkError: MutableLiveData<Boolean> = MutableLiveData(false)
    val networkError: LiveData<Boolean> = _networkError

    private val _exception: MutableLiveData<Boolean> = MutableLiveData(false)
    val exception: LiveData<Boolean> = _exception

    private var getAllGamePreviewsDisposable: Disposable? = null

    private val _gamePreviews: MutableLiveData<List<GamePreview>> = MutableLiveData()
    val gamePreviews: LiveData<List<GamePreview>> = _gamePreviews

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
            .doAfterSuccess {
                _loading.postValue(false)
            }
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.main())
            .subscribe { either -> processGamePreviewsEither(either) }
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