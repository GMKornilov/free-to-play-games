package com.gmkornilov.sberschool.freegames.presentation.features.gamelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.gmkornilov.sberschool.freegames.databinding.FragmentGamePreviewsBinding
import com.gmkornilov.sberschool.freegames.presentation.features.gamelist.adapter.PreviewsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameListFragment : Fragment() {
    private lateinit var binding: FragmentGamePreviewsBinding

    private val viewModel: GameListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGamePreviewsBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
//        setSupportActionBar(binding.toolbar)

        val adapter = PreviewsAdapter(viewModel)
        binding.mainContent.gameList.adapter = adapter

        viewModel.gamePreviews.observe(viewLifecycleOwner, {
            binding.mainContent.gameList.visibility = View.VISIBLE
            adapter.setData(it)
        })

        viewModel.loading.observe(viewLifecycleOwner, {
            binding.mainContent.loadingView.root.visibility = mapVisibility(it)
        })

        viewModel.serverError.observe(viewLifecycleOwner, {
            binding.mainContent.serverView.root.visibility = mapVisibility(it)
        })

        viewModel.networkError.observe(viewLifecycleOwner, {
            binding.mainContent.networkView.root.visibility = mapVisibility(it)
        })

        viewModel.exception.observe(viewLifecycleOwner, {
            binding.mainContent.unknownErrorView.root.visibility = mapVisibility(it)
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
        fun newInstance(): Fragment {
            return GameListFragment()
        }
    }
}