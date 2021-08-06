package com.gmkornilov.sberschool.freegames.presentation.features.gameinfo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import com.gmkornilov.sberschool.freegames.R
import com.gmkornilov.sberschool.freegames.databinding.ActivityGameInfoBinding
import com.gmkornilov.sberschool.freegames.domain.entity.gamepreview.GamePreview
import com.gmkornilov.sberschool.freegames.domain.entity.navigation.GameInfoNavigationInfo
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
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
        val titleTransitionName = intent?.getStringExtra(SHARED_TITLE_NAME)
        if (thumbnailTransitionName != null && titleTransitionName != null) {
            binding.gameImage.transitionName = thumbnailTransitionName
            binding.scrollContent.titleText.transitionName = titleTransitionName
        }

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
            binding.scrollContent.titleText.text = it.title
        })

        viewModel.gameInfo.observe(this, {
            binding.scrollContent.descriptionText.text = it.description
        })

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        @VisibleForTesting
        const val GAME_PREVIEW = "GAME_PREVIEW"

        @VisibleForTesting
        const val SHARED_TITLE_NAME = "SHARED_TITLE_NAME"

        @VisibleForTesting
        const val SHARED_THUMBNAIL_NAME = "SHARED_THUMBNAIL_NAME"

        fun newIntent(context: Context, gameInfoNavigationInfo: GameInfoNavigationInfo): Intent {
            return Intent(context, GameInfoActivity::class.java).apply {
                putExtra(GAME_PREVIEW, gameInfoNavigationInfo.gamePreview)
                putExtra(SHARED_TITLE_NAME, gameInfoNavigationInfo.sharedTitleName)
                putExtra(SHARED_THUMBNAIL_NAME, gameInfoNavigationInfo.sharedThumbnailName)
            }
        }
    }
}