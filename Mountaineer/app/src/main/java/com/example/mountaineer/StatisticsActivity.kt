package com.example.mountaineer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.room.Room
import com.example.mountaineer.dao.MountainExpeditionDao
import com.example.mountaineer.dao.MountainRangeDao
import com.example.mountaineer.database.AppDatabase
import com.example.mountaineer.model.MountainExpedition
import kotlinx.coroutines.runBlocking

class StatisticsActivity : AppCompatActivity() {

    private lateinit var mountainExpeditionDao: MountainExpeditionDao
    private lateinit var mountainRangeDao: MountainRangeDao
    private lateinit var amountTV: TextView
    private lateinit var highestTV: TextView
    private lateinit var mostVisitedRangeTV: TextView
    private lateinit var statisticsConstraintLayout: ConstraintLayout
    private lateinit var noStatisticsTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        title = "Statystyki"

        amountTV = findViewById(R.id.amountTV)
        highestTV = findViewById(R.id.highestTV)
        mostVisitedRangeTV = findViewById(R.id.mostVisitedRangeTV)
        statisticsConstraintLayout = findViewById(R.id.statisticsConstraintLayout)
        noStatisticsTV = findViewById(R.id.noStatisticsTV)


        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "mountaineer-database"
        ).build()

        mountainExpeditionDao = db.mountainExpeditionDao()
        mountainRangeDao = db.mountainRangeDao()

        if(getAmount()==0){
            statisticsConstraintLayout.visibility = View.INVISIBLE
            noStatisticsTV.visibility = View.VISIBLE
        } else{
            statisticsConstraintLayout.visibility = View.VISIBLE
            noStatisticsTV.visibility = View.INVISIBLE
            amountTV.text = getAmount().toString()
            highestTV.text = "${getHighestMountain().mountainName}\n(${getHighestMountain().mountainHeight} m n.p.m.)"
            mostVisitedRangeTV.text = getMostVisitedRange()
        }

    }

    private fun getAmount(): Int{
        var amount: Int
        runBlocking {
        amount =  mountainExpeditionDao.countMountainExpeditions()
        }
        return amount
    }

    private fun getHighestMountain(): MountainExpedition {
        var mountainExpedition: MountainExpedition
        runBlocking {
            mountainExpedition = mountainExpeditionDao.getHighestMountain()
        }
        return mountainExpedition
    }

    private fun getMostVisitedRange(): String{
        var mountainRange: String
        runBlocking {
            mountainRange =
                mountainRangeDao.getMountainRangeNameById(mountainExpeditionDao.getMostVisited()).toString()
        }
        return mountainRange
    }


}