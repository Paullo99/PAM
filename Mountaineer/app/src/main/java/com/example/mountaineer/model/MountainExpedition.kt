package com.example.mountaineer.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mountain_expedition")
data class MountainExpedition(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "mountain_name") val mountainName: String? = null,
    @ColumnInfo(name = "conquer_date") val conquerDate: String? = null
) {
    override fun toString(): String {
        return "MountainExpedition(id=$id, mountainName=$mountainName, conquerDate=$conquerDate)"
    }
}

