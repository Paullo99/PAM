package com.example.lab6

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity() {

    private lateinit var infoTextView: TextView
    private lateinit var telMgrTextView: TextView
    private lateinit var telMgr: TelephonyManager
    private var permission by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        infoTextView = findViewById(R.id.infoTextView)
        telMgrTextView = findViewById(R.id.telMgrTextView)
        telMgr = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    }

    override fun onStart() {
        super.onStart()
        val phoneStateListener: PhoneStateListener = object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, incomingNumber: String) {
                telMgrTextView.text = getTelephonyOverview().toString()
            }
        }
        telMgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
        val telephonyOverview: String = getTelephonyOverview().toString()
        this.telMgrTextView.text = telephonyOverview
    }

    @SuppressLint("MissingPermission")
    fun getTelephonyOverview(): Int {
        return telMgr.dataNetworkType
    }

    @SuppressLint("SetTextI18n", "MissingPermission")
    fun showTelephonyInfo(view: View) {
        getPermissions()
        if(permission == PackageManager.PERMISSION_GRANTED) {
            infoTextView.text = "Call state: " + telMgr.callState.toString() + "\n" +
                    "Phone type: " + telMgr.phoneType.toString() + "\n" +
                    "Network type: " + telMgr.networkType.toString() + "\n" +
                    "SIM Operator: " + telMgr.simOperator + "\n"
        }
    }

    private fun getPermissions() {
        permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_PHONE_STATE
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_PHONE_STATE),
                1
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Uprawnienia odrzucone, aplikacja może nie działać poprawnie!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Uprawnienia przyznane! Wciśnij przycisk ponownie", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

