package com.zhaoxi.plants.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zhaoxi.plants.dao.FavoritePlantDao
import com.zhaoxi.plants.model.Plant

@Database(entities = [Plant::class], version = 1, exportSchema = false)
abstract class FavoritePlantDatabase: RoomDatabase() {
    abstract fun favoritePlantDao(): FavoritePlantDao?

    companion object{
        private var instance: FavoritePlantDatabase? = null

        fun getInstance(context: Context): FavoritePlantDatabase{
            return instance?:synchronized(this){
                instance?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): FavoritePlantDatabase{
            return Room.databaseBuilder(context, FavoritePlantDatabase::class.java, "FavoritePlantDatabase").fallbackToDestructiveMigration().build()
        }
    }
}