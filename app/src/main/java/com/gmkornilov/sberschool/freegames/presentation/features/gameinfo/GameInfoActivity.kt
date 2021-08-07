package com.gmkornilov.sberschool.freegames.presentation.features.gameinfo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import com.gmkornilov.sberschool.freegames.R
import com.gmkornilov.sberschool.freegames.databinding.ActivityGameInfoBinding
import com.gmkornilov.sberschool.freegames.domain.entity.gamepreview.GamePreview
import com.gmkornilov.sberschool.freegames.domain.entity.navigation.GameInfoNavigationInfo
import com.gmkornilov.sberschool.freegames.presentation.features.gameinfo.adapter.ScreenshotAdapter
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GameInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameInfoBinding

    @Inject
    lateinit var gameInfoViewModelFactory: GameInfoViewModel.ViewModelAssistedFactory

    private val viewModel: GameInfoViewModel by viewModels {
        GameInfoViewModel.provideFactory(
            gameInfoViewModelFactory, intent.getSerializableExtra(
                GAME_PREVIEW
            ) as GamePreview
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameInfoBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        setContentView(binding.root)
        supportPostponeEnterTransition()

        val thumbnailTransitionName = intent?.getStringExtra(SHARED_THUMBNAIL_NAME)
        if (thumbnailTransitionName != null) {
            binding.gameImage.transitionName = thumbnailTransitionName
        }

        viewModel.serverError.observe(this, {
            binding.scrollContent.serverView.root.visibility = mapVisibility(it)
        })

        viewModel.networkError.observe(this, {
            binding.scrollContent.networkView.root.visibility = mapVisibility(it)
        })

        viewModel.exception.observe(this, {
            binding.scrollContent.unknownErrorView.root.visibility = mapVisibility(it)
        })

        viewModel.notFoundError.observe(this, {
            binding.scrollContent.notFoundView.root.visibility = mapVisibility(it)
        })


        viewModel.gamePreview.observe(this, {
            Picasso.get()
                .load(it.thumbnailUrl)
                .noFade()
                .into(binding.gameImage, object : Callback {
                    override fun onError(e: Exception?) {
                        supportStartPostponedEnterTransition()
                    }

                    override fun onSuccess() {
                        supportStartPostponedEnterTransition()
                    }
                })
            binding.toolbarLayout.title = it.title
        })

        viewModel.successfullyLoaded.observe(this, {
            binding.scrollContent.mainContentGroup.visibility = mapVisibility(it)
        })

        viewModel.requirements.observe(this, {
            binding.scrollContent.osText.text = it.os
            binding.scrollContent.processorText.text = it.processor
            binding.scrollContent.memoryText.text = it.memory
            binding.scrollContent.graphicsText.text = it.graphics
            binding.scrollContent.requirementGroup.visibility = View.VISIBLE
        })

        viewModel.developer.observe(this, {
            binding.scrollContent.developerText.text = it
        })

        viewModel.description.observe(this, {
            binding.scrollContent.descriptionText.text = it
        })

        val adapter = ScreenshotAdapter()
        binding.scrollContent.screenshotCarousel.apply {
            this.adapter = adapter
            setIntervalRatio(0.4f)
            setFlat(true)
        }
        viewModel.screenshots.observe(this, {
            adapter.setData(it)
        })

        viewModel.loading.observe(this, {
            binding.scrollContent.loadingLayout.visibility = mapVisibility(it)
            if (it) {
                binding.scrollContent.loadingLayout.startShimmer()

                binding.scrollContent.mainContentGroup.visibility = View.GONE
                binding.scrollContent.requirementGroup.visibility = View.GONE
            } else {
                binding.scrollContent.loadingLayout.stopShimmer()
            }
        })

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun mapVisibility(isVisible: Boolean): Int {
        return if (isVisible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        @VisibleForTesting
        const val GAME_PREVIEW = "GAME_PREVIEW"

        @VisibleForTesting
        const val SHARED_THUMBNAIL_NAME = "SHARED_THUMBNAIL_NAME"

        fun newIntent(context: Context, gameInfoNavigationInfo: GameInfoNavigationInfo): Intent {
            return Intent(context, GameInfoActivity::class.java).apply {
                putExtra(GAME_PREVIEW, gameInfoNavigationInfo.gamePreview)
                putExtra(SHARED_THUMBNAIL_NAME, gameInfoNavigationInfo.sharedThumbnailName)
            }
        }
    }
}