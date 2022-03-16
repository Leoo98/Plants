package com.zhaoxi.plants.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.zhaoxi.plants.R
import com.zhaoxi.plants.activity.PlantDetailActivity
import com.zhaoxi.plants.dao.FavoritePlantDao
import com.zhaoxi.plants.database.FavoritePlantDatabase
import com.zhaoxi.plants.model.Plant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlantStreamAdapter(private val context: Context, val editable: Boolean): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var plantList:ArrayList<Plant?> = ArrayList()
    private val TYPE_COMMON = 0
    private val TYPE_FOOTER = 1
    private var favoritePlantDatabaseDao: FavoritePlantDao? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == TYPE_COMMON){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.stream_item, parent, false)
            return PlantStreamViewHolder(view)
        }else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.footer_item, parent, false)
            return PlantStreamFooterViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is PlantStreamViewHolder) {
            val item = plantList[position]
            val url = item!!.coverURL
            holder.name.text = if (item.name == "") {
                "没有中文译名"
            } else {
                item.name
            }
            holder.area.text = if (item.area == "") {
                "没有分布信息"
            } else {
                item.area
            }
            Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.plantImg)
            GlobalScope.launch {
                withContext(Dispatchers.Default) {
                    favoritePlantDatabaseDao = FavoritePlantDatabase.getInstance(context).favoritePlantDao()
                }
                val record = favoritePlantDatabaseDao!!.getOneFavorite(item.plantId)
                if(record == null){
                    holder.likeBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_unlike))
                }else{
                    holder.likeBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_like))
                }
            }
            if(!editable){
                holder.likeBtn.isEnabled = false
            }

            holder.likeBtn.setOnClickListener{
                it as ImageButton
                if(holder.like){
                    GlobalScope.launch {
                        withContext(Dispatchers.Default) {
                            val record = favoritePlantDatabaseDao!!.getOneFavorite(item.plantId)
                            favoritePlantDatabaseDao!!.deleteOneFavorite(record!!)
                        }
                        it.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_unlike))
                        holder.like = false
                    }
                }else{
                    GlobalScope.launch {
                        withContext(Dispatchers.Default) {
                            favoritePlantDatabaseDao!!.insertOneFavorite(item)
                        }
                        it.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_like))
                        holder.like = true
                    }
                }
            }
            holder.itemView.setOnClickListener {
                val intent = Intent(context, PlantDetailActivity::class.java)
                intent.putExtra("plant_name", holder.name.text)
                intent.putExtra("plant_english_name", item.englishName)
                intent.putExtra("plant_img_url", item.coverURL)
                intent.putExtra("plant_area", holder.area.text)
                context.startActivity(intent)
            }
        }else if(holder is PlantStreamFooterViewHolder){
            holder.progressBar.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return plantList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(plantList[position] == null){TYPE_FOOTER}else{TYPE_COMMON}
    }

    fun updateStream(newPlantList: ArrayList<Plant?>){
        if(newPlantList.size < plantList.size){
            plantList = newPlantList
            notifyItemRemoved(plantList.size)
        }else{
            plantList = newPlantList
            notifyDataSetChanged()
        }

    }
}

