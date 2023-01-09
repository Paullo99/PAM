package com.example.mountaineer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.example.mountaineer.dao.MountainExpeditionDao
import com.example.mountaineer.database.AppDatabase

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.mountaineer.databinding.ActivityShowMountainExpeditionsOnMapBinding
import com.example.mountaineer.model.MountainExpedition
import kotlinx.coroutines.runBlocking

class ShowMountainExpeditionsOnMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityShowMountainExpeditionsOnMapBinding
    private lateinit var mountainExpeditionDao: MountainExpeditionDao
    private lateinit var mountainExpeditionList: List<MountainExpedition>
    private val centerOfPoland = LatLng(52.1,19.2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowMountainExpeditionsOnMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "mountaineer-database"
        ).build()

        mountainExpeditionDao = db.mountainExpeditionDao()
        runBlocking {
            mountainExpeditionList = mountainExpeditionDao.getAllMountainExpeditions()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val markerOptions = MarkerOptions()
        for(mountainExpedition: MountainExpedition in mountainExpeditionList){
            markerOptions.position(LatLng(mountainExpedition.latitude!!, mountainExpedition.longitude!!))
            mMap.addMarker(markerOptions)
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(centerOfPoland, 5f))
    }
}