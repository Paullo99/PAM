package com.example.mountaineer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.mountaineer.adapter.MainActivityRVAdapter
import com.example.mountaineer.dao.MountainExpeditionDao
import com.example.mountaineer.database.AppDatabase
import com.example.mountaineer.model.MountainExpedition
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var mountainExpeditionDao: MountainExpeditionDao
    private var counter = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "mountaineer-database"
        ).build()

        mountainExpeditionDao = db.mountainExpeditionDao()
        runBlocking {
            recyclerView.adapter = MainActivityRVAdapter(this@MainActivity, mountainExpeditionDao.getAllMountainExpeditions())
        }
    }

    fun add(view: View?) {
        val expedition = MountainExpedition(mountainName = "Rysy$counter", conquerDate = "2022-12-1$counter")

        runBlocking {
            val x = mountainExpeditionDao.insert(expedition)
            println(x)
            val expeditionList = mountainExpeditionDao.getAllMountainExpeditions()
            recyclerView.swapAdapter(MainActivityRVAdapter(this@MainActivity, expeditionList), false);
            recyclerView.scrollToPosition(expeditionList.size-1)
        }

        counter++
    }

}