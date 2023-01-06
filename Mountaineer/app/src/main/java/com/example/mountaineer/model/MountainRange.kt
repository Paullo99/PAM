package com.example.mountaineer.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mountain_range")
data class MountainRange (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "mountain_range_name") val name: String? = "",
){
    override fun toString(): String {
        return "$name"
    }
}