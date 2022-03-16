package com.zhaoxi.plants.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zhaoxi.plants.dao.FavoritePlantDao
import com.zhaoxi.plants.model.Plant

class MainViewModel(favoritePlantDao: FavoritePlantDao): ViewModel() {
    var plantList = MutableLiveData<ArrayList<Plant?>>()
    var favoritePlantList = favoritePlantDao.getAllFavorite()
    var isLoading = MutableLiveData<Boolean>()

    init{
        isLoading.value = false
        plantList.value = ArrayList()
    }

    fun updateList(newPlantList: ArrayList<Plant?>){
        plantList.value = newPlantList
    }

    fun insertNull(){
        val tempList = plantList.value
        tempList?.add(null)
        plantList.value = tempList
    }

    fun removeLast(){
        val tempList = plantList.value
        tempList?.removeAt(tempList.size - 1)
        plantList.value = tempList
    }

    fun startLoading(){
        isLoading.value = true
    }

    fun stopLoading(){
        isLoading.value = false
    }

    fun getIsLoading(): Boolean{
        return isLoading.value!!
    }
}