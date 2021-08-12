package com.gmkornilov.sberschool.freegames.presentation.features.gameinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.gmkornilov.sberschool.freegames.R
import com.gmkornilov.sberschool.freegames.databinding.FragmentGameInfoBinding
import com.gmkornilov.sberschool.freegames.domain.entity.gamepreview.GamePreview
import com.gmkornilov.sberschool.freegames.presentation.features.gameinfo.adapter.ScreenshotAdapter
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GameInfoFragment : Fragment() {

    private lateinit var binding: FragmentGameInfoBinding

    @Inject
    lateinit var gameInfoViewModelFactory: GameInfoViewModel.ViewModelAssistedFactory

    private val viewModel: GameInfoViewModel by viewModels {
        GameInfoViewModel.provideFactory(
            gameInfoViewModelFactory, requireArguments().getSerializable(
                GAME_PREVIEW
            ) as GamePreview
        )
    }

    fun setSharedName(sharedThumbnailName: String) {
        var args = arguments
        if (args == null) {
            args = Bundle()
        }
        args.putString(SHARED_THUMBNAIL_NAME, sharedThumbnailName)
        arguments = args
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameInfoBinding.inflate(inflater)
        binding.viewModel = viewModel
        postponeEnterTransition()

        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        val thumbnailTransitionName = requireArguments().getString(SHARED_THUMBNAIL_NAME)
        if (thumbnailTransitionName != null) {
            binding.gameImage.transitionName = thumbnailTransitionName
        }

        viewModel.serverError.observe(viewLifecycleOwner, {
            binding.scrollContent.serverView.root.visibility = mapVisibility(it)
        })

        viewModel.networkError.observe(viewLifecycleOwner, {
            binding.scrollContent.networkView.root.visibility = mapVisibility(it)
        })

        viewModel.notFoundError.observe(viewLifecycleOwner, {
            binding.scrollContent.notFoundView.root.visibility = mapVisibility(it)
        })

        viewModel.exception.observe(viewLifecycleOwner, {
            binding.scrollContent.unknownErrorView.root.visibility = mapVisibility(it)
        })

        viewModel.gamePreview.observe(viewLifecycleOwner, {
            Picasso.get()
                .load(it.thumbnailUrl)
                .noFade()
                .into(binding.gameImage, object : Callback {
                    override fun onError(e: Exception?) {
                        startPostponedEnterTransition()
                    }

                    override fun onSuccess() {
                        startPostponedEnterTransition()
                    }
                })
            binding.toolbarLayout.title = it.title
        })

        viewModel.successfullyLoaded.observe(viewLifecycleOwner, {
            binding.scrollContent.mainContentGroup.visibility = mapVisibility(it)
        })

        viewModel.requirements.observe(viewLifecycleOwner, {
            binding.scrollContent.osText.text = it.os
            binding.scrollContent.processorText.text = it.processor
            binding.scrollContent.memoryText.text = it.memory
            binding.scrollContent.graphicsText.text = it.graphics
            binding.scrollContent.requirementGroup.visibility = View.VISIBLE
        })

        viewModel.developer.observe(viewLifecycleOwner, {
            binding.scrollContent.developerText.text = it
        })

        viewModel.description.observe(viewLifecycleOwner, {
            binding.scrollContent.descriptionText.text = it
        })

        val adapter = ScreenshotAdapter()
        binding.scrollContent.screenshotCarousel.apply {
            this.adapter = adapter
            setIntervalRatio(0.4f)
            setFlat(true)
        }
        viewModel.screenshots.observe(viewLifecycleOwner, {
            adapter.setData(it)
        })

        viewModel.loading.observe(viewLifecycleOwner, {
            binding.scrollContent.loadingLayout.visibility = mapVisibility(it)
            if (it) {
                binding.scrollContent.loadingLayout.startShimmer()

                binding.scrollContent.mainContentGroup.visibility = View.GONE
                binding.scrollContent.requirementGroup.visibility = View.GONE
            } else {
                binding.scrollContent.loadingLayout.stopShimmer()
            }
        })

        return binding.root
    }

    private fun mapVisibility(isVisible: Boolean): Int {
        return if (isVisible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    companion object {
        @VisibleForTesting
        const val GAME_PREVIEW = "GAME_PREVIEW"

        @VisibleForTesting
        const val SHARED_THUMBNAIL_NAME = "SHARED_THUMBNAIL_NAME"

        fun newFragment(gameInfoNavigationInfo: GamePreview): Fragment {
            val bundle = Bundle().apply {
                putSerializable(GAME_PREVIEW, gameInfoNavigationInfo)
            }
            return GameInfoFragment().apply {
                arguments = bundle
            }
        }
    }
}