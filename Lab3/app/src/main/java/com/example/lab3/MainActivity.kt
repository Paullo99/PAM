package com.example.lab3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var firstNumberTextField: EditText
    private lateinit var secondNumberTextField:EditText
    private lateinit var resultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firstNumberTextField = findViewById(R.id.firstNumberTextField)
        secondNumberTextField = findViewById(R.id.secondNumberTextField)
        resultTextView = findViewById(R.id.resultTextView)
    }

    fun add(view : View?){
        if(firstNumberTextField.text.toString() != "" && secondNumberTextField.text.toString() != "") {
            val result = firstNumberTextField.text.toString().toDouble() + secondNumberTextField.text.toString().toDouble()
            resultTextView.text = result.toString()
            Log.i("Lab3", "Dodawanie powiodło się")
        }
        else
            Toast.makeText(this, "Wprowadź wszystkie potrzebne wartości!", Toast.LENGTH_LONG).show()
    }

    fun sub(view : View?){
        if(firstNumberTextField.text.toString() != "" && secondNumberTextField.text.toString() != "") {
            val result = firstNumberTextField.text.toString().toDouble() - secondNumberTextField.text.toString().toDouble()
            resultTextView.text = result.toString()
            Log.i("Lab3", "Odejmowanie powiodło się")
        }
        else
            Toast.makeText(this, "Wprowadź wszystkie potrzebne wartości!", Toast.LENGTH_LONG).show()
    }

    fun mul(view : View?){
        if(firstNumberTextField.text.toString() != "" && secondNumberTextField.text.toString() != "") {
            val result = firstNumberTextField.text.toString().toDouble() * secondNumberTextField.text.toString().toDouble()
            resultTextView.text = result.toString()
            Log.i("Lab3", "Mnożenie powiodło się")
        }
        else
            Toast.makeText(this, "Wprowadź wszystkie potrzebne wartości!", Toast.LENGTH_LONG).show()
    }

    fun div(view : View?){
        if(firstNumberTextField.text.toString() != "" && secondNumberTextField.text.toString() != "") {
            val result = firstNumberTextField.text.toString().toDouble() / secondNumberTextField.text.toString().toDouble()
            resultTextView.text = result.toString()
            Log.i("Lab3", "Dodawanie powiodło się")
        }
        else
            Toast.makeText(this, "Wprowadź wszystkie potrzebne wartości!", Toast.LENGTH_LONG).show()
    }
}