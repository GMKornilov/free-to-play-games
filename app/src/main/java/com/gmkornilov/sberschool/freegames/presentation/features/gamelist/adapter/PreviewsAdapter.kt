package com.gmkornilov.sberschool.freegames.presentation.features.gamelist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.gmkornilov.sberschool.freegames.R
import com.gmkornilov.sberschool.freegames.domain.entity.gamepreview.GamePreview
import com.gmkornilov.sberschool.freegames.presentation.navigation.GameInfoNavigationInfo
import com.squareup.picasso.Picasso

class PreviewsAdapter(
    private val gamePreviewClicked: GamePreviewClicked,
) : RecyclerView.Adapter<PreviewsAdapter.PreviewViewHolder>() {
    class PreviewViewHolder(
        private val view: View,
        private val gamePreviewClicked: GamePreviewClicked,
    ) : RecyclerView.ViewHolder(view) {
        private val titleText: TextView = view.findViewById(R.id.titleText)
        private val descriptionText: TextView = view.findViewById(R.id.descriptionText)
        private val thumbnailImage: ImageView =
            view.findViewById<ImageView>(R.id.thumbnailImage).apply {
                clipToOutline = true
            }

        private val shimmer = Shimmer.AlphaHighlightBuilder()
            .setDuration(500)
            .setBaseAlpha(0.7f)
            .setHighlightAlpha(0.6f)
            .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
            .build()

        private val shimmerDrawable = ShimmerDrawable().apply {
            setShimmer(shimmer)
        }

        fun bind(preview: GamePreview) {
            titleText.text = preview.title
            descriptionText.text = preview.description

            Picasso.get()
                .load(preview.thumbnailUrl)
                .placeholder(shimmerDrawable)
                .noFade()
                .fit()
                .centerCrop()
                .into(thumbnailImage)

            view.setOnClickListener {
                val gameInfoNavigationInfo = GameInfoNavigationInfo(
                    preview,
                    "thumbnail${preview.id}",
                    thumbnailImage,
                )
                gamePreviewClicked.onGamePreviewClicked(gameInfoNavigationInfo)
            }
        }
    }

    private var data: List<GamePreview> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.game_preview_item, parent, false)
        return PreviewViewHolder(view, gamePreviewClicked)
    }

    override fun onBindViewHolder(holder: PreviewViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(newData: List<GamePreview>) {
        data = newData
        notifyDataSetChanged()
    }
}