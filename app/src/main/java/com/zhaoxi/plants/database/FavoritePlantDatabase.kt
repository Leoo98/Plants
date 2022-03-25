package com.zhaoxi.plants.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zhaoxi.plants.dao.FavoritePlantDao
import com.zhaoxi.plants.model.Plant

@Database(entities = [Plant::class], version = 1, exportSchema = false)
abstract class FavoritePlantDatabase: RoomDatabase() {
    abstract fun favoritePlantDao(): FavoritePlantDao
}