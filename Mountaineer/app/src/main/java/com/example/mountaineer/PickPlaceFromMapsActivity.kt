package com.example.mountaineer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.mountaineer.databinding.ActivityPickPlaceFromMapsBinding
import com.google.android.gms.maps.model.Marker

class PickPlaceFromMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityPickPlaceFromMapsBinding
    private var currentMarker: Marker? = null
    private var currentLocation = LatLng(50.102005, 18.546258)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPickPlaceFromMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_pick_place_from_maps, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.approveLocationButton) {
            val intent = Intent()
            intent.putExtra("latitude", currentLocation.latitude)
            intent.putExtra("longitude", currentLocation.longitude)
            setResult(RESULT_OK, intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        drawMarker(currentLocation)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 10f))

        mMap.setOnMapClickListener { p0 -> setNewCurrentLocation(p0) }

        mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDrag(p0: Marker) {}

            override fun onMarkerDragEnd(p0: Marker) {
                setNewCurrentLocation(p0.position)
            }

            override fun onMarkerDragStart(p0: Marker) {}
        })
    }

    private fun drawMarker(latlong: LatLng) {
        val markerOptions =
            MarkerOptions().position(latlong).title("Wybrana lokalizacja").draggable(true)
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latlong))
        currentMarker = mMap.addMarker(markerOptions)
        currentMarker?.showInfoWindow()
    }

    private fun setNewCurrentLocation(latLng: LatLng) {
        currentMarker?.remove()
        currentLocation = LatLng(latLng.latitude, latLng.longitude)
        drawMarker(currentLocation)
    }
}