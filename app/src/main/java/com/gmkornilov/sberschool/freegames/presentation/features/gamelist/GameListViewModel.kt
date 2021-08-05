package com.gmkornilov.sberschool.freegames.presentation.features.gamelist

import androidx.lifecycle.ViewModel
import com.gmkornilov.sberschool.freegames.domain.entity.gamepreview.GamePreview
import com.gmkornilov.sberschool.freegames.domain.interactor.SingleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameListViewModel @Inject constructor(
    private val getAllGamePreviewsUseCase: SingleUseCase<List<GamePreview>, Unit>,
) : ViewModel() {

}