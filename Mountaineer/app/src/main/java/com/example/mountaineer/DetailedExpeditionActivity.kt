package com.example.mountaineer

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.room.Room
import com.example.mountaineer.dao.MountainExpeditionDao
import com.example.mountaineer.database.AppDatabase
import com.example.mountaineer.model.MountainExpedition
import kotlinx.coroutines.runBlocking

class DetailedExpeditionActivity : AppCompatActivity() {

    private lateinit var mountainExpeditionDao: MountainExpeditionDao
    private lateinit var mountainExpedition: MountainExpedition

    private lateinit var mountainNameTV: TextView
    private lateinit var mountainRangeTV: TextView
    private lateinit var heightTV: TextView
    private lateinit var dateTV: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_expedition)

        mountainNameTV = findViewById(R.id.mountainNameTV)
        mountainRangeTV = findViewById(R.id.mountainRangeTV)
        heightTV = findViewById(R.id.heightTV)
        dateTV = findViewById(R.id.dateTV)

        val id = intent.getIntExtra("id",1)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "mountaineer-database"
        ).build()

        mountainExpeditionDao = db.mountainExpeditionDao()
        runBlocking {
            mountainExpedition = mountainExpeditionDao.getMountainExpeditionById(id)
        }

        mountainNameTV.text = mountainExpedition.mountainName
        mountainRangeTV.text = mountainExpedition.mountainRange
        heightTV.text = mountainExpedition.mountainHeight.toString() + " m n.p.m."
        dateTV.text = mountainExpedition.conquerDate
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detailed_expedition, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

            if(item.itemId == R.id.deleteExpeditionButton){
                val alertDialogBuilder = AlertDialog.Builder(this)
                alertDialogBuilder.setMessage("Czy na pewno chcesz usunąć zdobyty szczyt?")
                alertDialogBuilder.setPositiveButton("Tak"){ _, _ ->
                    runBlocking {
                        mountainExpeditionDao.delete(mountainExpedition)
                    }
                    setResult(RESULT_OK, Intent().putExtra("deleted", "true"))
                    finish()
                }
                alertDialogBuilder.setNegativeButton("Nie", null)
                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
        }
        return super.onOptionsItemSelected(item)
    }
}