package com.zhaoxi.plants.network

import android.util.Log
import com.zhaoxi.plants.model.Plant
import com.zhaoxi.plants.model.PlantInfo
import com.zhaoxi.plants.service.PlantStreamService
import com.zhaoxi.plants.viewmodel.MainViewModel
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkUtil private constructor(){

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl("https://api.apishop.net/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service: PlantStreamService = retrofit.create(PlantStreamService::class.java)

    companion object{
        val instance: NetworkUtil by lazy{
            NetworkUtil()
        }
    }

    fun refreshPlantList(mainViewModel: MainViewModel){
        val page: Int = (1..5).random()
        val call = service.getInfo("uhJNsu69ef4db151a4e0a1a75e7bc94da2719e7bee93c4c",
            page)

        call.enqueue(object : retrofit2.Callback<PlantInfo>{
            override fun onResponse(
                call: Call<PlantInfo>,
                response: Response<PlantInfo>
            ) {
                val result = response.body()
                if(result != null)
                    result.result.plantList?.let { mainViewModel.updateList(it) }
            }

            override fun onFailure(call: Call<PlantInfo>, t: Throwable) {
                Log.d("Zhaoxi", t.toString())
            }
        })
    }

    fun loadMore(mainViewModel: MainViewModel){
        val page: Int = (1..5).random()
        val call = service.getInfo("uhJNsu69ef4db151a4e0a1a75e7bc94da2719e7bee93c4c",
            page)
        call.enqueue(object : retrofit2.Callback<PlantInfo>{
            override fun onResponse(
                call: Call<PlantInfo>,
                response: Response<PlantInfo>
            ) {
                val result = response.body()

                if(result != null){
                    val tempList = mainViewModel.plantList.value
                    mainViewModel.removeLast()
                    result.result.plantList?.let { tempList?.addAll(it) }
                    mainViewModel.updateList(tempList as ArrayList<Plant?>)
                    mainViewModel.stopLoading()
                }
            }

            override fun onFailure(call: Call<PlantInfo>, t: Throwable) {
                Log.d("Zhaoxi", t.toString())
                mainViewModel.removeLast()
                mainViewModel.stopLoading()
            }
        })
    }


}