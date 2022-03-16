package com.zhaoxi.plants.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File

class DetailViewModel: ViewModel() {
    var videoPath = MutableLiveData<Uri>()

    fun updatePath(rootFile: File, plantName:String){
        if(!rootFile.exists()){
            rootFile.mkdir()
        }
        val path = rootFile.path+"/VID_${plantName}.mp4"
        if(File(path).exists()){
            videoPath.value = Uri.parse(path)
        }

    }
}