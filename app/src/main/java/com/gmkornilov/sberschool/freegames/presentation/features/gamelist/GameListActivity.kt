package com.gmkornilov.sberschool.freegames.presentation.features.gamelist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.gmkornilov.sberschool.freegames.R
import com.gmkornilov.sberschool.freegames.databinding.ActivityGamePreviewsBinding
import com.gmkornilov.sberschool.freegames.presentation.features.gamelist.adapter.PreviewsAdapter
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.util.Pair

@AndroidEntryPoint
class GameListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGamePreviewsBinding

    private val viewModel: GameListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGamePreviewsBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val adapter = PreviewsAdapter(viewModel)
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

        viewModel.startActivityEvent.observe(this, {
            val imageUtilPair =
                Pair.create(it.second.sharedImageView as View, it.second.sharedThumbnailName)
            val titleUtilPair =
                Pair.create(it.second.sharedTitle as View, it.second.sharedTitleName)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                imageUtilPair,
                titleUtilPair
            )
            startActivity(it.first, options.toBundle())
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