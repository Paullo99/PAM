package com.example.lab6

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast

class SmsReceiver : BroadcastReceiver() {

    private val smsRecAction = "android.provider.Telephony.SMS_RECEIVED"

    override fun onReceive(context: Context, intent: Intent?) {

        if (intent!!.action.equals(smsRecAction)) {
            val sb = StringBuilder()
            val bundle = intent.extras
            if (bundle != null) {
                val pdus = bundle["pdus"] as Array<*>?
                var receiverPhone = ""
                for (pdu in pdus!!) {
                    val smsMessage: SmsMessage = SmsMessage.createFromPdu(pdu as ByteArray)
                    receiverPhone = smsMessage.displayOriginatingAddress
                    sb.append(smsMessage.displayMessageBody)
                }
                val smsManager = SmsManager.getDefault()
                val receivedValue = sb.toString().trim()
                if (receivedValue.startsWith("PILNE"))
                    Toast.makeText(context, "Otrzymano SMSa o treści: $receivedValue", Toast.LENGTH_LONG)
                        .show()
                try {
                    if (receivedValue.toInt() < 10)
                        smsManager.sendTextMessage(
                            receiverPhone,
                            null,
                            receivedValue.toInt().plus(1).toString(),
                            null,
                            null
                        )
                } catch (_: java.lang.Exception) {
                }
            }
        }
    }
}
