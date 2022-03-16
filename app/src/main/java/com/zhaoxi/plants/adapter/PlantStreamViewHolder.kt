package com.zhaoxi.plants.adapter

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zhaoxi.plants.R

class PlantStreamViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val plantImg: ImageView = itemView.findViewById(R.id.plant_img)
    val name: TextView = itemView.findViewById(R.id.name)
    val area: TextView = itemView.findViewById(R.id.area)
    val likeBtn: ImageButton = itemView.findViewById(R.id.like_btn)
    var like: Boolean = false
}