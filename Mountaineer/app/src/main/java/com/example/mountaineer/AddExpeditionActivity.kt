package com.example.mountaineer

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.room.Room
import com.example.mountaineer.dao.MountainExpeditionDao
import com.example.mountaineer.database.AppDatabase
import com.example.mountaineer.model.MountainExpedition
import kotlinx.coroutines.runBlocking
import java.util.*

class AddExpeditionActivity : AppCompatActivity() {

    private lateinit var mountainExpeditionDao: MountainExpeditionDao
    private lateinit var mountainNameEditText: EditText
    private lateinit var mountainRangeEditText: EditText
    private lateinit var mountainHeightEditText: EditText
    private lateinit var conquerDateEditText: TextView
    private lateinit var calendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Nowy zdobyty szczyt"
        setContentView(R.layout.activity_add_expedition)

        mountainNameEditText = findViewById(R.id.mountainNameEditText)
        mountainRangeEditText = findViewById(R.id.mountainRangeEditText)
        mountainHeightEditText = findViewById(R.id.mountainHeightEditText)
        conquerDateEditText = findViewById(R.id.conquerDateEditText)

        calendar = Calendar.getInstance()

        val todayDate = "%d-%02d-%02d".format(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH)+1,
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        conquerDateEditText.text = todayDate

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "mountaineer-database"
        ).build()

        mountainExpeditionDao = db.mountainExpeditionDao()

    }

    fun changeDate(view: View?) {
        class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

            override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                return DatePickerDialog(requireContext(), this, year, month, day)
            }

            override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
                val finalString = "$year-%02d-%02d".format(month + 1, day)
                conquerDateEditText.text = finalString
            }
        }
        DatePickerFragment().show(supportFragmentManager, "datePicker")
    }

    fun addNewExpedition(view: View?) {
        val mountainExpedition = MountainExpedition(
            mountainName = mountainNameEditText.text.toString(),
            mountainRange = mountainRangeEditText.text.toString(),
            mountainHeight = Integer.parseInt(mountainHeightEditText.text.toString()),
            conquerDate = conquerDateEditText.text.toString()
        )
        runBlocking {
            mountainExpeditionDao.insert(mountainExpedition)
        }
        setResult(RESULT_OK)
        finish()
    }
}