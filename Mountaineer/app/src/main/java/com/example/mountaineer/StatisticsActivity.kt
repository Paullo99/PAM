package com.example.mountaineer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.room.Room
import com.example.mountaineer.dao.MountainExpeditionDao
import com.example.mountaineer.dao.MountainRangeDao
import com.example.mountaineer.database.AppDatabase
import com.example.mountaineer.databinding.ActivityStatisticsBinding
import com.example.mountaineer.model.MountainExpedition
import kotlinx.coroutines.runBlocking

class StatisticsActivity : AppCompatActivity() {

    private lateinit var mountainExpeditionDao: MountainExpeditionDao
    private lateinit var mountainRangeDao: MountainRangeDao
    private lateinit var binding: ActivityStatisticsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatisticsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Statystyki"

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "mountaineer-database"
        ).build()

        mountainExpeditionDao = db.mountainExpeditionDao()
        mountainRangeDao = db.mountainRangeDao()

        if (getAmount() == 0) {
            binding.statisticsConstraintLayout.visibility = View.INVISIBLE
            binding.noStatisticsTV.visibility = View.VISIBLE
        } else {
            binding.statisticsConstraintLayout.visibility = View.VISIBLE
            binding.noStatisticsTV.visibility = View.INVISIBLE
            binding.amountTV.text = getAmount().toString()
            binding.highestTV.text =
                "${getHighestMountain().mountainName}\n(${getHighestMountain().mountainHeight} m n.p.m.)"
            binding.mostVisitedRangeTV.text = getMostVisitedRange()
        }

    }

    private fun getAmount(): Int {
        var amount: Int
        runBlocking {
            amount = mountainExpeditionDao.countMountainExpeditions()
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

    private fun getMostVisitedRange(): String {
        var mountainRange: String
        runBlocking {
            mountainRange =
                mountainRangeDao.getMountainRangeNameById(mountainExpeditionDao.getMostVisited())
                    .toString()
        }
        return mountainRange
    }


}