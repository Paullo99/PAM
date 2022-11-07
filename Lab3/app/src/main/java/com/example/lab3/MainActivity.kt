package com.example.lab3

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var amountTextField: EditText
    private lateinit var maxTipTextField:EditText
    private lateinit var totalAmountTextView: TextView
    private lateinit var tipTextView: TextView
    private lateinit var serviceQualityBar: RatingBar
    private lateinit var foodQualityBar: SeekBar
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var roundTipSwitch: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        amountTextField = findViewById(R.id.amountTextField)
        maxTipTextField = findViewById(R.id.maxTipTextField)
        totalAmountTextView = findViewById(R.id.totalAmountTextView)
        tipTextView = findViewById(R.id.tipTextView)
        serviceQualityBar = findViewById(R.id.serviceQualityBar)
        foodQualityBar = findViewById(R.id.foodQualityBar)
        roundTipSwitch = findViewById(R.id.roundTipSwitch)
    }

    @SuppressLint("SetTextI18n")
    fun calculateTip(view : View?){
        if(amountTextField.text.toString() != "" && maxTipTextField.text.toString() != ""){
            val amount = amountTextField.text.toString().toDouble()
            val maxTip = maxTipTextField.text.toString().toDouble() / 100

            val maxServiceRating = serviceQualityBar.numStars
            val maxFoodRating = foodQualityBar.max

            val currentServiceRating = serviceQualityBar.rating
            val currentFoodRating = foodQualityBar.progress

            val serviceRating = currentServiceRating / maxServiceRating
            val foodRating = currentFoodRating.toDouble() / maxFoodRating

            val tip = maxTip * amount * serviceRating * foodRating
            val totalAmount = amount + tip

            if(roundTipSwitch.isChecked){
                tipTextView.text = tip.roundToInt().toString() + " zł"
                totalAmountTextView.text = totalAmount.roundToInt().toString() + " zł"
            }else {
                tipTextView.text = ((tip * 100).roundToInt() / 100.0).toString() + " zł"
                totalAmountTextView.text = ((totalAmount * 100).roundToInt() / 100.0).toString() + " zł"
            }
        }
        else {
            Toast.makeText(this, "Wprowadź wszystkie dane!", Toast.LENGTH_SHORT).show()
            tipTextView.text = ""
            totalAmountTextView.text = ""
        }

        // Schowanie klawiatury po wciśnięciu przycisku
        try {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } catch (e: Exception) {
            Log.i("Lab3", "Error: $e")
        }
    }

}