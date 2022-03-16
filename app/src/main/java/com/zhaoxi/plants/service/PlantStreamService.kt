package com.zhaoxi.plants.service

import com.zhaoxi.plants.model.PlantInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface PlantStreamService {
    @GET("common/plantFamily/queryPlantList")
    fun getInfo( @Query("apiKey") apiKey: String,
                 @Query("page") page: Int = 1,
                 @Query("pageSize") pageSize: Int = 30): Call<PlantInfo>

}