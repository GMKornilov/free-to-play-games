package com.gmkornilov.sberschool.freegames.presentation.features.gamelist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import com.gmkornilov.sberschool.freegames.R
import com.gmkornilov.sberschool.freegames.databinding.ActivityMainBinding
import com.gmkornilov.sberschool.freegames.presentation.features.gamelist.adapter.PreviewsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: GameListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val adapter = PreviewsAdapter()
        binding.mainContent.gameList.adapter = adapter

        viewModel.gamePreviews.observe(this, {
            adapter.setData(it)
        })

        viewModel.loading.observe(this, {
            binding.mainContent.loadingView.root.visibility = mapVisibility(it)
        })

        viewModel.serverError.observe(this, {
            binding.mainContent.serverView.root.visibility = mapVisibility(it)
        })

        viewModel.networkError.observe(this, {
            binding.mainContent.networkView.root.visibility = mapVisibility(it)
        })

        viewModel.exception.observe(this, {
            binding.mainContent.unknownErrorView.root.visibility = mapVisibility(it)
        })

        viewModel.successfullyLoaded.observe(this, {
            binding.mainContent.gameList.visibility = mapVisibility(it)
        })
    }

    private fun mapVisibility(isVisible: Boolean): Int {
        return if (isVisible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}