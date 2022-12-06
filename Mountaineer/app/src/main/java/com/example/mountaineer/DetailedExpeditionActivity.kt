package com.example.mountaineer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class DetailedExpeditionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_expedition)

        val id = intent.getIntExtra("id",1)
        Toast.makeText(this, id.toString(), Toast.LENGTH_SHORT).show()
    }
}