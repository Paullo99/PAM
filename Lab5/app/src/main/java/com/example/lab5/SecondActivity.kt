package com.example.lab5

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class SecondActivity : AppCompatActivity() {

    private lateinit var studentDataTextView: TextView
    private lateinit var myCallerIntent: Intent
    private var studentName = "Paweł Maciończyk 5.0"
    private var noStudentInBase = "Brak studenta w bazie :("

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        studentDataTextView = findViewById(R.id.studentDataTextView)

        myCallerIntent = intent
        val myBundle = myCallerIntent.extras!!
        val index = myBundle.getString("index").toString()

        if (index.equals("248837")) {
            studentDataTextView.text = "Znaleziono studenta! \n".plus(studentName)
            myBundle.putString("studentName", studentName)
        } else {
            studentDataTextView.text = noStudentInBase
            myBundle.putString("studentName", noStudentInBase)
        }
        myCallerIntent.putExtras(myBundle)
    }

    fun sendStudentDataAndFinish(view: View) {
        setResult(Activity.RESULT_OK, myCallerIntent)
        finish()
    }

}