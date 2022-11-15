package com.example.lab6battery

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class PowerConnectionReceiver : BroadcastReceiver(){

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent!!.action.equals(Intent.ACTION_POWER_CONNECTED))
            Toast.makeText(context, "Podłączono do ładowania", Toast.LENGTH_SHORT).show()

        if (intent.action.equals(Intent.ACTION_POWER_DISCONNECTED))
            Toast.makeText(context, "Odłączono od ładowania", Toast.LENGTH_SHORT).show()

        if (intent.action.equals(Intent.ACTION_BATTERY_LOW))
            Toast.makeText(context, "Niski poziom baterii", Toast.LENGTH_SHORT).show()

        if (intent.action.equals(Intent.ACTION_BATTERY_OKAY))
            Toast.makeText(context, "Bateria OK", Toast.LENGTH_SHORT).show()
    }
}
