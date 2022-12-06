package com.example.lab7

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.lab7.adapter.ItemAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var specialisationEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var amountOfStudentsEditText: EditText
    private lateinit var editButton: Button

    private lateinit var dbHandler: MyDBHandler

    private lateinit var fieldOfStudyFound: FieldOfStudy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nameEditText = findViewById(R.id.nameEditText)
        specialisationEditText = findViewById(R.id.specialisationEditText)
        recyclerView = findViewById(R.id.recyclerView)
        amountOfStudentsEditText = findViewById(R.id.amountOfStudentsEditText)
        editButton = findViewById(R.id.editButton)
        editButton.isEnabled = false

        dbHandler = MyDBHandler(this, null, null, 1)
        val fieldsOfStudy = dbHandler.findAllFieldsOfStudy()
        recyclerView.adapter = ItemAdapter(this, fieldsOfStudy)
    }


    @SuppressLint("NotifyDataSetChanged")
    fun newFieldOfStudy(view: View) {

        if (specialisationEditText.text.toString() == "" || nameEditText.text.toString() == ""
            || amountOfStudentsEditText.text.toString() == ""
        ) {
            Toast.makeText(this, "Wprowadź wszystkie dane!", Toast.LENGTH_SHORT).show()
            return
        }

        val amountOfStudents = Integer.parseInt(amountOfStudentsEditText.text.toString().trim())
        val fieldOfStudy = FieldOfStudy(
            nameEditText.text.toString().trim(),
            specialisationEditText.text.toString().trim(),
            amountOfStudents
        )

        dbHandler.addFieldOfStudy(fieldOfStudy)
        nameEditText.setText("")
        specialisationEditText.setText("")
        amountOfStudentsEditText.setText("")
        recyclerView.adapter?.notifyDataSetChanged()
    }

    fun lookupFieldOfStudy(view: View) {
        fieldOfStudyFound = dbHandler.findFieldOfStudy(nameEditText.text.toString().trim())!!

        specialisationEditText.setText(fieldOfStudyFound.specialisation)
        amountOfStudentsEditText.setText(fieldOfStudyFound.amountOfStudents.toString())
        editButton.isEnabled = true
    }

    @SuppressLint("NotifyDataSetChanged")
    fun editFieldOfStudy(view: View) {
        val fieldOfStudyEdited = FieldOfStudy(
            fieldOfStudyFound.id,
            nameEditText.text.toString(), specialisationEditText.text.toString(),
            Integer.parseInt(amountOfStudentsEditText.text.toString())
        )
        dbHandler.editFieldOfStudy(fieldOfStudyEdited)
        nameEditText.setText("")
        specialisationEditText.setText("")
        amountOfStudentsEditText.setText("")
        recyclerView.adapter?.notifyDataSetChanged()
        editButton.isEnabled = false
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeFieldOfStudy(view: View) {
        val result = dbHandler.deleteFieldOfStudy(nameEditText.text.toString().trim())

        if (result) {
            nameEditText.setText("")
            specialisationEditText.setText("")
            recyclerView.adapter?.notifyDataSetChanged()
        } else
            Toast.makeText(this, "Nie ma takiego kierunku", Toast.LENGTH_SHORT).show()
    }
}