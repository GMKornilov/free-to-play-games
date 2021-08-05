package com.gmkornilov.sberschool.freegames.presentation.features.gamelist

import android.util.Log
import androidx.lifecycle.LiveData
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
    val serverErrorPublishSubject: PublishSubject<Unit> = PublishSubject.create()
    val networkErrorPublishSubject: PublishSubject<Unit> = PublishSubject.create()
    val exceptionPublishSubject: PublishSubject<Unit> = PublishSubject.create()

    private var getAllGamePreviewsDisposable: Disposable? = null

    private val _gamePreviews: MutableLiveData<List<GamePreview>> = MutableLiveData()
    val gamePreviews: LiveData<List<GamePreview>> = _gamePreviews

    private val _loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    val sucessfullyLoaded: LiveData<Boolean> = _loading

    init {
        getAllGamePreviews()
    }

    /**
     * Loads previews of all games and stores them in LiveData
     */
    fun getAllGamePreviews() {
        getAllGamePreviewsDisposable?.dispose()

        getAllGamePreviewsDisposable = getAllGamePreviewsUseCase.buildSingle(Unit)
            .doOnSubscribe {
                _loading.postValue(true)
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
        // we don't have any feature-specific failures in this call, so we don't process them
        when (failure) {
            is Failure.ExceptionFailure -> {
                Log.d(TAG, "Unknown exception:", failure.t)
                exceptionPublishSubject.onNext(Unit)
            }
            Failure.NetworkConnection -> networkErrorPublishSubject.onNext(Unit)
            Failure.ServerError -> serverErrorPublishSubject.onNext(Unit)
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