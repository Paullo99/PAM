package com.example.mountaineer

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.mountaineer.adapter.MainActivityRVAdapter
import com.example.mountaineer.adapter.OnItemClickListener
import com.example.mountaineer.dao.MountainExpeditionDao
import com.example.mountaineer.database.AppDatabase
import com.example.mountaineer.model.MountainExpedition
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var mountainExpeditionDao: MountainExpeditionDao
    private lateinit var mountainExpeditionList: List<MountainExpedition>
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
            mountainExpeditionList = mountainExpeditionDao.getAllMountainExpeditions()
        }
        recyclerView.adapter = MainActivityRVAdapter(mountainExpeditionList, this)
    }

    fun add(view: View?) {
        val expedition = MountainExpedition(mountainName = "Rysy$counter", conquerDate = "2022-12-1$counter")

        runBlocking {
            mountainExpeditionDao.insert(expedition)
            mountainExpeditionList = mountainExpeditionDao.getAllMountainExpeditions()
            recyclerView.swapAdapter(MainActivityRVAdapter(mountainExpeditionList, this@MainActivity), false);
            recyclerView.scrollToPosition(mountainExpeditionList.size-1)
        }

        counter++
    }

    private fun getSelectedExpedition(id: Int) {
        val intent = Intent(this, DetailedExpeditionActivity::class.java)
        intent.putExtra("id", id)
        detailedExpeditionActivityLauncher.launch(intent)
    }

    //Receiver
    private val detailedExpeditionActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            println("Welcome back")
        }
    }

    override fun onItemClick(position: Int) {
        getSelectedExpedition(mountainExpeditionList[position].id)
    }


}