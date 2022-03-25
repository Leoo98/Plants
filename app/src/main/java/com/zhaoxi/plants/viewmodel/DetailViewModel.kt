package com.zhaoxi.plants.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(): ViewModel() {
    private val _videoPath = MutableLiveData<Uri>()
    val videoPath: LiveData<Uri> = _videoPath

    fun updatePath(rootFile: File, plantName:String){
        if(!rootFile.exists()){
            rootFile.mkdir()
        }
        val path = rootFile.path+"/VID_${plantName}.mp4"
        if(File(path).exists()){
            _videoPath.value = Uri.parse(path)
        }

    }
}