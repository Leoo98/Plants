package com.zhaoxi.plants.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zhaoxi.plants.dao.FavoritePlantDao
import java.lang.Exception

class MainViewModelFactory(private val favoritePlantDao: FavoritePlantDao): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        var viewModel: ViewModel? = null
        var className = modelClass.name
        try{
            viewModel = Class.forName(className).getConstructor(FavoritePlantDao::class.java).newInstance(favoritePlantDao) as ViewModel
        }catch (e: Exception){
            e.printStackTrace()
        }
        return viewModel as T
    }
}