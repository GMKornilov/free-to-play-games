package com.gmkornilov.sberschool.freegames.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Replace
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.gmkornilov.sberschool.freegames.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: AppCompatActivity(R.layout.activity_main) {
    @Inject
    lateinit var navigationHolder: NavigatorHolder

    private val navigator = object : AppNavigator(this, R.id.container) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            navigator.applyCommands(arrayOf(Replace(Screens.GamePreviewsList())))
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigationHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigationHolder.removeNavigator()
        super.onPause()
    }
}