package com.example.mountaineer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.room.Room
import com.example.mountaineer.dao.MountainExpeditionDao
import com.example.mountaineer.database.AppDatabase
import com.example.mountaineer.model.MountainExpedition
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private lateinit var mountainExpeditionDao: MountainExpeditionDao
    private var counter = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "mountaineer-database"
        ).build()

        mountainExpeditionDao = db.mountainExpeditionDao()

    }

    fun add(view: View?) {
        val expedition = MountainExpedition(mountainName = "Rysy$counter")

        runBlocking {
            mountainExpeditionDao.insert(expedition)
        }
        counter++
    }

    fun viewExpeditions(view: View?) {
        runBlocking {
            val x = mountainExpeditionDao.getAllMountainExpeditions()
            for (y in x)
                println(y)
        }
    }
}