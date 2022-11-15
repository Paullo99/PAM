package com.example.lab6

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class OutgoingCallReceiver : BroadcastReceiver() {

    private val abortPhoneNumber = "+48721821410"

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent!!.action.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            val phoneNumber: String? = intent.extras?.getString(Intent.EXTRA_PHONE_NUMBER)
            if (phoneNumber != null && phoneNumber.equals(abortPhoneNumber)) {
                Toast.makeText(
                    context, "Połączenie wychodzące zostało zablokowane",
                    Toast.LENGTH_LONG
                ).show()
                if (this.resultData != null)
                    this.resultData = null
            }
        }
    }
}