package com.example.mountaineer.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mountaineer.dao.MountainExpeditionDao
import com.example.mountaineer.model.MountainExpedition

@Database(entities = [MountainExpedition::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mountainExpeditionDao(): MountainExpeditionDao
}