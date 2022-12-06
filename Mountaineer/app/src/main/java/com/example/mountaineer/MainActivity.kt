package com.example.mountaineer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.room.Room
import com.example.mountaineer.dao.MountainExpeditionDao
import com.example.mountaineer.database.AppDatabase
import com.example.mountaineer.model.MountainExpedition

class MainActivity : AppCompatActivity() {

    private lateinit var mountainExpeditionDao: MountainExpeditionDao
    private var counter = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "mountaineer-database"
        ).allowMainThreadQueries()
            .build()

        mountainExpeditionDao = db.mountainExpeditionDao()

    }

    fun add(view: View?) {
        val expedition = MountainExpedition(mountainName = "Rysy$counter")
        mountainExpeditionDao.insert(expedition)
        counter++
    }

    fun viewExpeditions(view: View?) {
        val x = mountainExpeditionDao.getAllMountainExpeditions()

        for (y in x)
            println(y)
    }
}