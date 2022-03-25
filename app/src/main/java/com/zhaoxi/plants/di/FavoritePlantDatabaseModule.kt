package com.zhaoxi.plants.di

import android.content.Context
import androidx.room.Room
import com.zhaoxi.plants.dao.FavoritePlantDao
import com.zhaoxi.plants.database.FavoritePlantDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FavoritePlantDatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): FavoritePlantDatabase {
        return Room.databaseBuilder(context, FavoritePlantDatabase::class.java, "FavoritePlantDatabase").fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideFavoritePlantDao(database: FavoritePlantDatabase): FavoritePlantDao{
        return database.favoritePlantDao()
    }
}