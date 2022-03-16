package com.zhaoxi.plants.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "favorite_plant")
data class Plant(
    @PrimaryKey(autoGenerate = true)
    @Expose
    var id:Long = 0,
    @ColumnInfo(name = "cover_url")
    val coverURL: String,
    @ColumnInfo(name = "plant_name")
    val name: String,
    @ColumnInfo(name = "plant_area")
    val area: String,
    @SerializedName("engName")
    @ColumnInfo(name = "plant_eng_name")
    val englishName: String,
    @SerializedName("plantID")
    @ColumnInfo(name = "plant_id")
    val plantId: Int
)