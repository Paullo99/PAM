package com.example.mountaineer.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mountaineer.dao.MountainExpeditionDao
import com.example.mountaineer.dao.MountainRangeDao
import com.example.mountaineer.model.MountainExpedition
import com.example.mountaineer.model.MountainRange

@Database(entities = [MountainExpedition::class, MountainRange::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mountainExpeditionDao(): MountainExpeditionDao
    abstract fun mountainRangeDao(): MountainRangeDao
}