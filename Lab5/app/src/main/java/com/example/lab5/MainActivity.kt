package com.example.lab5

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings.ACTION_BLUETOOTH_SETTINGS
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var longitudeEditText: EditText
    private lateinit var latitudeEditText: EditText
    private lateinit var studentTextView: TextView
    private lateinit var pickedContactTextView: TextView
    private lateinit var indexNumberEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        longitudeEditText = findViewById(R.id.longitudeEditText)
        latitudeEditText = findViewById(R.id.latitudeEditText)
        studentTextView = findViewById(R.id.studentTextView)
        pickedContactTextView = findViewById(R.id.pickedContactTextView)
        indexNumberEditText = findViewById(R.id.indexNumberEditText)

    }

    fun showBluetoothSettings(view: View) {
        val intent = Intent(ACTION_BLUETOOTH_SETTINGS)
        startActivity(intent)
    }

    fun showCoordinates(view: View) {
        val geoCode =
            "geo:" + latitudeEditText.text.toString() + "," + longitudeEditText.text.toString() + "?z=16"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(geoCode))
        startActivity(intent)
    }

    fun pickContact(view: View) {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
        }
        startActivityForResult(intent, 123)
    }

    fun findStudentByIndex(view: View) {
        val intent = Intent(this@MainActivity, SecondActivity::class.java)
        val myDataBundle = Bundle()

        myDataBundle.putString("index", indexNumberEditText.text.toString())

        intent.putExtras(myDataBundle)
        startActivityForResult(intent, 1122)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1122 && resultCode == RESULT_OK) {
            val myResultBundle = data?.extras
            val formattedString = myResultBundle!!.getString("studentName")
            studentTextView.text = formattedString
        }

        if (requestCode == 123 && resultCode == RESULT_OK) {
            val contactUri: Uri? = data!!.data
            val projection: Array<String> = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)
            if (contactUri != null) {
                contentResolver.query(contactUri, projection, null, null, null).use { cursor ->
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            val numberIndex =
                                cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                            val number = cursor.getString(numberIndex)
                            pickedContactTextView.text = "Numer telefonu: " + number
                        }
                    }
                }
            }
        }


    }

}