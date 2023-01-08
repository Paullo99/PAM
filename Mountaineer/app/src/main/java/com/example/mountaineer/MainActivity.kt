package com.example.mountaineer

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase.CONFLICT_IGNORE
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mountaineer.adapter.MainActivityRVAdapter
import com.example.mountaineer.adapter.OnItemClickListener
import com.example.mountaineer.dao.MountainExpeditionDao
import com.example.mountaineer.database.AppDatabase
import com.example.mountaineer.model.MountainExpedition
import kotlinx.coroutines.runBlocking


class MainActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var noMountainExpeditionsTV: TextView
    private lateinit var mountainExpeditionDao: MountainExpeditionDao
    private lateinit var mountainExpeditionList: List<MountainExpedition>
    private val polishMountainRangeList = listOf(
        "Góry Świętokrzyskie", "Karkonosze", "Tatry",
        "Pieniny", "Beskid Śląski", "Beskid Mały", "Beskid Żywiecki", "Bieszczady", "Inne"
    ).sorted()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)
        noMountainExpeditionsTV = findViewById(R.id.noMountainExpeditionsTV)

        val db = buildDatabase()

        mountainExpeditionDao = db.mountainExpeditionDao()
        runBlocking {
            mountainExpeditionList = mountainExpeditionDao.getAllMountainExpeditions()
        }

        refreshView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.showStatisticsButton) {
            val intent = Intent(this, StatisticsActivity::class.java)
            statisticsActivityLauncher.launch(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    fun addNewMountainExpedition(view: View?) {
        val intent = Intent(this, AddExpeditionActivity::class.java)
        addNewExpeditionExpeditionActivityLauncher.launch(intent)
    }

    fun sortAlphabetDown(view: View?) {
        mountainExpeditionList = mountainExpeditionList.sortedBy { it.mountainName }
        refreshView()
    }

    fun sortAlphabetUp(view: View?) {
        mountainExpeditionList = mountainExpeditionList.sortedByDescending { it.mountainName }
        refreshView()
    }

    fun sortDateUp(view: View?) {
        mountainExpeditionList = mountainExpeditionList.sortedBy { it.conquerDate }
        refreshView()
    }

    fun sortDateDown(view: View?) {
        mountainExpeditionList = mountainExpeditionList.sortedByDescending { it.conquerDate }
        refreshView()
    }

    override fun onItemClick(position: Int) {
        getSelectedExpedition(mountainExpeditionList[position].id)
    }

    private fun getSelectedExpedition(id: Int) {
        val intent = Intent(this, DetailedExpeditionActivity::class.java)
        intent.putExtra("id", id)
        detailedExpeditionActivityLauncher.launch(intent)
    }

    private val detailedExpeditionActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK && it.data?.extras?.get("deleted") != null) {
            runBlocking {
                mountainExpeditionList = mountainExpeditionDao.getAllMountainExpeditions()
            }
            refreshView()
            Toast.makeText(this, "Usunięto zdobyty szczyt", Toast.LENGTH_SHORT).show()
        }
    }

    private val addNewExpeditionExpeditionActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "Dodano nowy zdobyty szczyt!", Toast.LENGTH_SHORT).show()
            runBlocking {
                mountainExpeditionList = mountainExpeditionDao.getAllMountainExpeditions()
            }
            refreshView()
            recyclerView.scrollToPosition(mountainExpeditionList.size - 1)
        } else {
            Toast.makeText(this, "Nie dodano nowego zdobytego szczytu.", Toast.LENGTH_SHORT).show()
        }
    }

    private val statisticsActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {}

    private fun refreshView() {
        if (mountainExpeditionList.isEmpty()) noMountainExpeditionsTV.visibility = View.VISIBLE
        else noMountainExpeditionsTV.visibility = View.INVISIBLE
        recyclerView.swapAdapter(
            MainActivityRVAdapter(
                mountainExpeditionList,
                this@MainActivity,
                this@MainActivity
            ), false
        )
    }

    private fun buildDatabase() = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java, "mountaineer-database"
    )
        .addCallback(insertInitialDataCallback)
        .build()

    private val insertInitialDataCallback: RoomDatabase.Callback =
        object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                insertPolishMountainRangesToDatabase(db)
            }
        }

    private fun insertPolishMountainRangesToDatabase(db: SupportSQLiteDatabase) {
        val cv = ContentValues()
        for (mountainRange in polishMountainRangeList) {
            cv.put("mountain_range_name", mountainRange)
            db.insert("mountain_range", CONFLICT_IGNORE, cv)
            cv.clear()
        }
    }


}