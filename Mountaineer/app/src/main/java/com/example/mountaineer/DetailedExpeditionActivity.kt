package com.example.mountaineer

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.room.Room
import com.example.mountaineer.dao.MountainExpeditionDao
import com.example.mountaineer.dao.MountainRangeDao
import com.example.mountaineer.database.AppDatabase
import com.example.mountaineer.helper.ImageRotator
import com.example.mountaineer.model.MountainExpedition
import kotlinx.coroutines.runBlocking
import java.io.File

class DetailedExpeditionActivity : AppCompatActivity() {

    private lateinit var mountainExpeditionDao: MountainExpeditionDao
    private lateinit var mountainRangeDao: MountainRangeDao
    private lateinit var mountainExpedition: MountainExpedition

    private lateinit var mountainNameTV: TextView
    private lateinit var mountainRangeTV: TextView
    private lateinit var heightTV: TextView
    private lateinit var dateTV: TextView
    private lateinit var photoIV: ImageView
    private lateinit var photoFile: File
    private var photoFileName: String = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_expedition)

        mountainNameTV = findViewById(R.id.mountainNameTV)
        mountainRangeTV = findViewById(R.id.mountainRangeTV)
        heightTV = findViewById(R.id.heightTV)
        dateTV = findViewById(R.id.dateTV)
        photoIV = findViewById(R.id.photoIV)

        val id = intent.getIntExtra("id", 1)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "mountaineer-database"
        ).build()

        mountainExpeditionDao = db.mountainExpeditionDao()
        mountainRangeDao = db.mountainRangeDao()
        runBlocking {
            mountainExpedition = mountainExpeditionDao.getMountainExpeditionById(id)
            mountainRangeTV.text =
                mountainRangeDao.getMountainRangeNameById(mountainExpedition.mountainRangeId)
                    .toString()
        }

        mountainNameTV.text = mountainExpedition.mountainName
        heightTV.text = mountainExpedition.mountainHeight.toString() + " m n.p.m."
        dateTV.text = mountainExpedition.conquerDate
        photoFileName = mountainExpedition.photoFileName
        setImageView()
    }

    private fun setImageView() {
        if (photoFileName != "") {
            photoFile = File(getExternalFilesDir(null), photoFileName)
            val imageRotator = ImageRotator()
            photoIV.setImageBitmap(imageRotator.getImageOriginalOrientation(photoFile))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detailed_expedition, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.deleteExpeditionButton) {
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setMessage("Czy na pewno chcesz usunąć zdobyty szczyt?")
            alertDialogBuilder.setPositiveButton("Tak") { _, _ ->
                runBlocking {
                    mountainExpeditionDao.delete(mountainExpedition)
                }
                setResult(RESULT_OK, Intent().putExtra("deleted", "true"))
                finish()
            }
            alertDialogBuilder.setNegativeButton("Nie", null)
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        } else if (item.itemId == R.id.editExpeditionButton) {
            val intent = Intent(this, EditMountainExpeditionActivity::class.java)
            editMountainExpeditionActivityLauncher.launch(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    private val editMountainExpeditionActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {}
}