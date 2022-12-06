package com.example.mountaineer.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mountaineer.model.MountainExpedition

@Dao
interface MountainExpeditionDao {

    @Query("SELECT * FROM mountain_expedition")
    suspend fun getAllMountainExpeditions(): List<MountainExpedition>

    @Query("SELECT * FROM mountain_expedition WHERE id = :id")
    suspend fun getMountainExpeditionById(id: Int): MountainExpedition

    @Insert
    suspend fun insert(expedition: MountainExpedition)
}