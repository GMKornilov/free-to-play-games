package com.gmkornilov.sberschool.freegames.presentation.features.gamelist

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.gmkornilov.sberschool.freegames.databinding.ActivityGamePreviewsBinding
import com.gmkornilov.sberschool.freegames.presentation.features.gamelist.adapter.PreviewsAdapter
import dagger.hilt.android.AndroidEntryPoint

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
            binding.mainContent.gameList.visibility = View.VISIBLE
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

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }
}