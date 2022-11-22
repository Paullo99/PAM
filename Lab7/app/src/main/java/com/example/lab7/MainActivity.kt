package com.example.lab7

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.lab7.adapter.ItemAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var quantityEditText: EditText
    private lateinit var resultTextView: TextView
    private lateinit var recyclerView: RecyclerView

    private lateinit var dbHandler : MyDBHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nameEditText = findViewById(R.id.nameEditText)
        quantityEditText = findViewById(R.id.quantityEditText)
        resultTextView = findViewById(R.id.resultTextView)
        recyclerView = findViewById(R.id.recyclerView)

        dbHandler = MyDBHandler(this, null, null, 1)
        val products = dbHandler.findAllProducts()
        recyclerView.adapter = ItemAdapter(this, products)
    }


    @SuppressLint("NotifyDataSetChanged")
    fun newProduct(view: View) {

        if(quantityEditText.text.toString().equals("") || nameEditText.text.toString().equals("")){
            Toast.makeText(this, "Wprowadź wszystkie dane!", Toast.LENGTH_SHORT).show()
            return
        }

        val quantity = Integer.parseInt(quantityEditText.text.toString().trim())
        val product = Product(nameEditText.text.toString().trim(), quantity)

        dbHandler.addProduct(product)
        nameEditText.setText("")
        quantityEditText.setText("")
        recyclerView.adapter?.notifyDataSetChanged()
    }

    fun lookupProduct(view: View) {
        val product = dbHandler.findProduct(nameEditText.text.toString().trim())

        if (product != null)
            resultTextView.text = product.toString()
        else
            Toast.makeText(this, "Nie ma takiego produktu", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeProduct(view: View) {
        val result = dbHandler.deleteProduct(nameEditText.text.toString().trim())

        if (result) {
            nameEditText.setText("")
            quantityEditText.setText("")
            recyclerView.adapter?.notifyDataSetChanged()
        } else
            Toast.makeText(this, "Nie ma takiego produktu", Toast.LENGTH_SHORT).show()
    }
}