package com.gmkornilov.sberschool.freegames.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.transition.ChangeBounds
import com.github.terrakok.cicerone.Back
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Replace
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.gmkornilov.sberschool.freegames.R
import com.gmkornilov.sberschool.freegames.presentation.features.gameinfo.GameInfoFragment
import com.gmkornilov.sberschool.freegames.presentation.features.gamelist.GameListFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    @Inject
    lateinit var navigationHolder: NavigatorHolder

    private val navigator = object : AppNavigator(this, R.id.container) {
        override fun setupFragmentTransaction(
            screen: FragmentScreen,
            fragmentTransaction: FragmentTransaction,
            currentFragment: Fragment?,
            nextFragment: Fragment
        ) {
            if (currentFragment is GameListFragment && nextFragment is GameInfoFragment) {
                setupSharedElementTransition(currentFragment, nextFragment, fragmentTransaction)
            }
        }
    }

    private fun setupSharedElementTransition(
        currentFragment: GameListFragment,
        nextFragment: GameInfoFragment,
        fragmentTransaction: FragmentTransaction
    ) {
        val view = currentFragment.sharedImageView
        val sharedTitle = currentFragment.sharedTitle

        if (sharedTitle == null || view == null) {
            return
        }

        view.transitionName = sharedTitle
        fragmentTransaction.addSharedElement(view, sharedTitle)
        nextFragment.setSharedName(sharedTitle)

        val transitionAnimation = ChangeBounds()
        currentFragment.sharedElementEnterTransition = transitionAnimation
        currentFragment.sharedElementReturnTransition = transitionAnimation
        nextFragment.sharedElementEnterTransition = transitionAnimation
        nextFragment.sharedElementReturnTransition = transitionAnimation

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            navigator.applyCommands(arrayOf(Replace(Screens.GamePreviewsList())))
        }
    }

    override fun onBackPressed() {
        navigator.applyCommands(arrayOf(Back()))
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