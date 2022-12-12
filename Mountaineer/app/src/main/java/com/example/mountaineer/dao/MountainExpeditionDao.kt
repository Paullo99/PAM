package com.example.mountaineer.dao

import androidx.room.Dao
import androidx.room.Delete
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

    @Delete
    suspend fun delete(expedition: MountainExpedition)

    @Query("SELECT COUNT(*) FROM mountain_expedition")
    suspend fun countMountainExpeditions(): Int

    @Query("SELECT * FROM mountain_expedition WHERE mountain_height = (SELECT MAX(mountain_height) FROM mountain_expedition)")
    suspend fun getHighestMountain(): MountainExpedition

    @Query("SELECT mountain_range FROM mountain_expedition GROUP BY mountain_range ORDER BY COUNT(mountain_range) DESC LIMIT 1")
    suspend fun getMostVisited(): String
}