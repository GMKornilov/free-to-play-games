package com.gmkornilov.sberschool.freegames.presentation.features.gameinfo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.gmkornilov.sberschool.freegames.R
import com.gmkornilov.sberschool.freegames.databinding.ActivityGameInfoBinding
import com.gmkornilov.sberschool.freegames.domain.entity.gameinfo.GameInfoNavigationInfo
import com.gmkornilov.sberschool.freegames.domain.entity.gamepreview.GamePreview
import com.squareup.picasso.Picasso
import javax.inject.Inject

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

        viewModel.gamePreview.observe(this, {
            Picasso.get().load(it.thumbnailUrl).into(binding.gameImage)
        })

        viewModel.gamePreview.observe(this, {

        })

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
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