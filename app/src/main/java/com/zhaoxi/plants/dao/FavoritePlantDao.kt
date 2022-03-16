package com.zhaoxi.plants.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zhaoxi.plants.model.Plant


@Dao
interface FavoritePlantDao {
    @Query("select * from favorite_plant where plant_id = :plantId")
    fun getOneFavorite(plantId: Int): Plant?

    @Query("select * from favorite_plant")
    fun getAllFavorite(): LiveData<MutableList<Plant?>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOneFavorite(favoritePlant: Plant): Long

    @Delete
    fun deleteOneFavorite(favoritePlant: Plant)
}