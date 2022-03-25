package com.zhaoxi.plants.activity

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.zhaoxi.plants.R
import com.zhaoxi.plants.dao.FavoritePlantDao
import com.zhaoxi.plants.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class PlantDetailActivity: AppCompatActivity() {
    private lateinit var plantVideo: VideoView
    val detailViewModel: DetailViewModel by viewModels()
    private var mediaStorageDir: File? = null
    private var plantNameText: String? = null
    private var plantEnglishNameText: String? = null
    private var plantDescText: String? = null
    private var plantImgUrlText: String? = null
    @Inject
    lateinit var favoritePlantDao: FavoritePlantDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.plant_detail)
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener{
            this.finish()
        }
        val plantImageView: ImageView = findViewById(R.id.plant_img)
        val plantName: TextView = findViewById(R.id.plant_name)
        val plantEnglishName: TextView? = findViewById(R.id.plant_english_name)
        val plantDesc: TextView = findViewById(R.id.plant_desc)
        val intent = intent
        plantVideo = findViewById(R.id.plant_vid)
        plantNameText = intent.getStringExtra("plant_name")
        plantEnglishNameText = intent.getStringExtra("plant_english_name")
        plantDescText = intent.getStringExtra("plant_area")
        plantImgUrlText = intent.getStringExtra("plant_img_url")
        plantName.text = plantNameText
        plantEnglishName!!.text = plantEnglishNameText
        plantDesc.text = plantDescText
        val plantImgUrl: String? = plantImgUrlText
        mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (mediaStorageDir != null) {
            detailViewModel.updatePath(mediaStorageDir!!, plantNameText!!)
        }
        Glide.with(this).load(plantImgUrl).into(plantImageView)
        val enterShootBtn: FloatingActionButton = findViewById(R.id.shoot_video_btn)
        enterShootBtn.setOnClickListener{
            val shootIntent = Intent(this, VideoShootActivity::class.java)
            shootIntent.putExtra("plant_name", plantName.text)
            startActivityForResult(shootIntent, 1)
        }
        plantVideo.visibility = View.GONE
        plantVideo.setOnClickListener{
            if(plantVideo.isPlaying){
                plantVideo.pause()
            }else{
                plantVideo.start()
            }
        }
        plantVideo.setOnPreparedListener {
            plantVideo.requestFocus()
            it.start()
            it.isLooping = true
        }
        detailViewModel.videoPath.observe(this){
            if(!plantVideo.isVisible)
                plantVideo.visibility = View.VISIBLE
            plantVideo.setVideoURI(it)
            plantVideo.requestFocus()
            plantVideo.start()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1 && resultCode == RESULT_OK){
            plantVideo.visibility = View.VISIBLE
            detailViewModel.updatePath(mediaStorageDir!!, plantNameText!!)
        }
    }
}