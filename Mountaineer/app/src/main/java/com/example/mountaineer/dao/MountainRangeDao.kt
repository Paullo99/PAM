package com.example.mountaineer.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.mountaineer.model.MountainRange

@Dao
interface MountainRangeDao {

    @Query("SELECT * FROM mountain_range WHERE id = :id")
    suspend fun getMountainRangeNameById(id: Int): MountainRange

    @Query("SELECT id FROM mountain_range WHERE mountain_range_name = :name")
    suspend fun getMountainRangeIdByName(name: String): Int

    @Query("SELECT * FROM mountain_range")
    suspend fun getAllMountainRanges(): List<MountainRange>
}