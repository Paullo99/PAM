package com.example.mountaineer.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mountaineer.model.MountainExpedition

@Dao
interface MountainExpeditionDao {

    @Query("SELECT * FROM mountain_expedition")
    suspend fun getAllMountainExpeditions(): List<MountainExpedition>

    @Insert
    suspend fun insert(expedition: MountainExpedition)
}