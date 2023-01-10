package com.example.mountaineer

import android.Manifest
import android.annotation.SuppressLint
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
import com.example.mountaineer.dao.MountainRangeDao
import com.example.mountaineer.database.AppDatabase
import com.example.mountaineer.databinding.ActivityAddExpeditionBinding
import com.example.mountaineer.helper.ImageRotator
import com.example.mountaineer.model.MountainExpedition
import com.example.mountaineer.model.MountainRange
import com.example.mountaineer.viewmodel.AddExpeditionActivityViewModel
import kotlinx.coroutines.runBlocking
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates


class AddExpeditionActivity : AppCompatActivity() {

    private lateinit var mountainExpeditionDao: MountainExpeditionDao
    private lateinit var mountainRangeDao: MountainRangeDao
    private lateinit var binding: ActivityAddExpeditionBinding
    private lateinit var calendar: Calendar
    private var photoFileName: String = ""
    private var latitude: Double? = null
    private var longitude: Double? = null
    private lateinit var photoFile: File
    private var permission by Delegates.notNull<Int>()
    private lateinit var viewModel: AddExpeditionActivityViewModel
    private lateinit var mountainRangeAdapter: ArrayAdapter<MountainRange>
    private lateinit var mountainRanges: List<MountainRange>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Nowy zdobyty szczyt"
        binding = ActivityAddExpeditionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        calendar = Calendar.getInstance()

        viewModel = ViewModelProvider(this)[AddExpeditionActivityViewModel::class.java]

        binding.mountainNameEditText.setText(viewModel.mountainName)
        binding.mountainHeightEditText.setText(viewModel.height)
        binding.mountainRangeSpinner.setSelection(viewModel.mountainRangePosiotion)
        binding.conquerDateEditText.text = viewModel.conquerDate
        if (viewModel.latitude == null)
            binding.locationTextView.setText("brak lokalizacji")
        else binding.locationTextView.setText("${viewModel.latitude}\n" +
                "${viewModel.longitude}")
        if (viewModel.photoFileName != "") {
            photoFileName = viewModel.photoFileName
            photoFile = File(getExternalFilesDir(null), photoFileName)
            val imageRotator = ImageRotator()
            binding.photoImageView.setImageBitmap(imageRotator.getImageOriginalOrientation(photoFile))
        }

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "mountaineer-database"
        ).build()

        mountainExpeditionDao = db.mountainExpeditionDao()
        mountainRangeDao = db.mountainRangeDao()

        runBlocking {
            mountainRanges = mountainRangeDao.getAllMountainRanges()
        }
        mountainRangeAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, mountainRanges)
        mountainRangeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.mountainRangeSpinner.adapter = mountainRangeAdapter
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
                binding.conquerDateEditText.text = finalString
                viewModel.conquerDate = finalString
            }
        }
        DatePickerFragment().show(supportFragmentManager, "datePicker")
    }

    fun addNewExpedition(view: View?) {
        if (checkIfAllInputsAreFilled()) {
            runBlocking {
                val mountainExpedition = MountainExpedition(
                    mountainName = binding.mountainNameEditText.text.toString(),
                    mountainRangeId = mountainRangeDao.getMountainRangeIdByName(binding.mountainRangeSpinner.selectedItem.toString()),
                    mountainHeight = Integer.parseInt(binding.mountainHeightEditText.text.toString()),
                    conquerDate = binding.conquerDateEditText.text.toString(),
                    latitude = latitude,
                    longitude = longitude,
                    photoFileName = photoFileName
                )
                mountainExpeditionDao.insert(mountainExpedition)
            }
            setResult(RESULT_OK)
            finish()
        } else {
            Toast.makeText(this, "Nie wypełniono wszystkich danych", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkIfAllInputsAreFilled(): Boolean {
        return binding.mountainNameEditText.text.toString() != ""
                && binding.mountainHeightEditText.text.toString() != ""
                && binding.conquerDateEditText.text.toString() != ""
                && longitude != null
                && latitude != null
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

    fun openMaps(view: View?) {
        val intent = Intent(this, PickPlaceFromMapsActivity::class.java)
        mapsActivityLauncher.launch(intent)
    }

    @SuppressLint("SetTextI18n")
    private val mapsActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            latitude = it.data?.extras?.get("latitude") as Double?
            longitude = it.data?.extras?.get("longitude") as Double?
            binding.locationTextView.text = "$latitude\n$longitude"
            viewModel.latitude = latitude
            viewModel.longitude = longitude
        } else {
            Toast.makeText(this, "Nie wybrano lokalizacji", Toast.LENGTH_SHORT).show()
        }
    }

    private val takePhotoIntentLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            photoFile = File(getExternalFilesDir(null), photoFileName)
            val imageRotator = ImageRotator()
            binding.photoImageView.setImageBitmap(imageRotator.getImageOriginalOrientation(photoFile))
        } else {
            photoFileName = ""
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
                }
            }
        }
    }
}