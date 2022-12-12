package com.example.mountaineer

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.mountaineer.dao.MountainExpeditionDao
import com.example.mountaineer.database.AppDatabase
import com.example.mountaineer.helper.ImageRotator
import com.example.mountaineer.model.MountainExpedition
import com.example.mountaineer.viewmodel.AddExpeditionActivityViewModel
import kotlinx.coroutines.runBlocking
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates


class AddExpeditionActivity : AppCompatActivity() {

    private lateinit var mountainExpeditionDao: MountainExpeditionDao
    private lateinit var mountainNameEditText: EditText
    private lateinit var mountainRangeEditText: EditText
    private lateinit var mountainHeightEditText: EditText
    private lateinit var conquerDateTextView: TextView
    private lateinit var calendar: Calendar
    private lateinit var photoImageView: ImageView
    private lateinit var photoFileName: String
    private lateinit var photoFile: File
    private var permission by Delegates.notNull<Int>()
    private lateinit var viewModel: AddExpeditionActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Nowy zdobyty szczyt"
        setContentView(R.layout.activity_add_expedition)

        mountainNameEditText = findViewById(R.id.mountainNameEditText)
        mountainRangeEditText = findViewById(R.id.mountainRangeEditText)
        mountainHeightEditText = findViewById(R.id.mountainHeightEditText)
        conquerDateTextView = findViewById(R.id.conquerDateEditText)
        photoImageView = findViewById(R.id.photoImageView)

        calendar = Calendar.getInstance()

        viewModel = ViewModelProvider(this)[AddExpeditionActivityViewModel::class.java]
        mountainNameEditText.setText(viewModel.mountainName)
        mountainRangeEditText.setText(viewModel.mountainRange)
        mountainHeightEditText.setText(viewModel.height)
        conquerDateTextView.text = viewModel.conquerDate
        if(viewModel.photoFileName!=""){
            photoFileName = viewModel.photoFileName
            photoFile = File(getExternalFilesDir(null), photoFileName)
            val imageRotator = ImageRotator()
            photoImageView.setImageBitmap(imageRotator.getImageOriginalOrientation(photoFile))
        }


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
                conquerDateTextView.text = finalString
                viewModel.conquerDate = finalString
            }
        }
        DatePickerFragment().show(supportFragmentManager, "datePicker")
    }

    fun addNewExpedition(view: View?) {
        if (checkIfAllInputsAreFilled()) {
            val mountainExpedition = MountainExpedition(
                mountainName = mountainNameEditText.text.toString(),
                mountainRange = mountainRangeEditText.text.toString(),
                mountainHeight = Integer.parseInt(mountainHeightEditText.text.toString()),
                conquerDate = conquerDateTextView.text.toString(),
                photoFileName = photoFileName
            )
            runBlocking {
                mountainExpeditionDao.insert(mountainExpedition)
            }
            setResult(RESULT_OK)
            finish()
        } else {
            Toast.makeText(this, "Nie wypełniono wszystkich danych", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkIfAllInputsAreFilled(): Boolean {
        return mountainNameEditText.text.toString() != ""
                && mountainRangeEditText.text.toString() != ""
                && mountainHeightEditText.text.toString() != ""
                && conquerDateTextView.text.toString() != ""
    }

    fun takePhoto(view: View?) {
        getCameraPermissions()
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        photoFileName = "$timeStamp.jpg"
        viewModel.photoFileName = photoFileName
        photoFile = File(getExternalFilesDir(null), photoFileName)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(
            MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(
                this, BuildConfig.APPLICATION_ID + ".provider",
                photoFile
            )
        )
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            takePhotoIntentLauncher.launch(intent)
        }
    }

    private val takePhotoIntentLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            photoFile = File(getExternalFilesDir(null), photoFileName)
            val imageRotator = ImageRotator()
            photoImageView.setImageBitmap(imageRotator.getImageOriginalOrientation(photoFile))
        }
    }

    private fun getCameraPermissions() {
        permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                1
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {

                if (grantResults.isEmpty() || ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    Toast.makeText(
                        this,
                        "W celu zrobienia zdjęcia konieczne są uprawnienia.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    takePhotoIntentLauncher.launch(intent)
                }
            }
        }
    }
}