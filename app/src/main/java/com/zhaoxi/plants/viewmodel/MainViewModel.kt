package com.zhaoxi.plants.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.zhaoxi.plants.dao.FavoritePlantDao
import com.zhaoxi.plants.model.Plant

class MainViewModel(favoritePlantDao: FavoritePlantDao): ViewModel() {
    private val _plantList = MutableLiveData<ArrayList<Plant?>>()
    val plantList:LiveData<ArrayList<Plant?>> = _plantList
    val favoritePlantList: LiveData<List<Plant?>> = favoritePlantDao.getAllFavorite().asLiveData()
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init{
        _isLoading.value = false
        _plantList.value = ArrayList()
    }

    fun updateList(newPlantList: ArrayList<Plant?>){
        _plantList.value = newPlantList
    }

    fun insertNull(){
        val tempList = plantList.value
        tempList?.add(null)
        _plantList.value = tempList!!
    }

    fun removeLast(){
        val tempList = plantList.value
        tempList?.removeAt(tempList.size - 1)
        _plantList.value = tempList!!
    }

    fun startLoading(){
        _isLoading.value = true
    }

    fun stopLoading(){
        _isLoading.value = false
    }

    fun getIsLoading(): Boolean{
        return _isLoading.value!!
    }
}