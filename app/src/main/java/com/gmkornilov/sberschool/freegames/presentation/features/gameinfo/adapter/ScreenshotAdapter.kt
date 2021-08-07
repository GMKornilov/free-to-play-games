package com.gmkornilov.sberschool.freegames.presentation.features.gameinfo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.gmkornilov.sberschool.freegames.R
import com.gmkornilov.sberschool.freegames.domain.entity.gameinfo.Screenshot
import com.squareup.picasso.Picasso

class ScreenshotAdapter : RecyclerView.Adapter<ScreenshotAdapter.ScreenshotViewHolder>() {
    class ScreenshotViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image: ImageView = view.findViewById(R.id.screenshotImage)

        fun bind(screenshot: Screenshot) {
            Picasso.get()
                .load(screenshot.imageUrl)
                .placeholder(R.drawable.ic_baseline_image_24)
                .noFade()
                .fit()
                .centerCrop()
                .into(image)
        }
    }

    private var data: List<Screenshot> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScreenshotViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.screenshot_item_layout, parent, false)
        return ScreenshotViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScreenshotViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(newData: List<Screenshot>) {
        data = newData
        notifyDataSetChanged()
    }
}