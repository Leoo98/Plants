package com.zhaoxi.plants.dao

import androidx.room.*
import com.zhaoxi.plants.model.Plant
import kotlinx.coroutines.flow.Flow


@Dao
interface FavoritePlantDao {
    @Query("select * from favorite_plant where plant_id = :plantId")
    fun getOneFavorite(plantId: Int): Flow<Plant?>?

    @Query("select * from favorite_plant")
    fun getAllFavorite(): Flow<List<Plant?>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOneFavorite(favoritePlant: Plant): Long

    @Delete
    suspend fun deleteOneFavorite(favoritePlant: Plant)
}