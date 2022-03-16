package com.zhaoxi.plants.adapter

import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.zhaoxi.plants.R

class PlantStreamFooterViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    var progressBar: ProgressBar = itemView.findViewById(R.id.loading_bar)
}