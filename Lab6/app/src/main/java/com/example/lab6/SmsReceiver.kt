package com.example.lab6

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.widget.Toast

class SmsReceiver : BroadcastReceiver() {

    private val smsRecAction = "android.provider.Telephony.SMS_RECEIVED"

    override fun onReceive(context: Context, intent: Intent?) {
        //val sendIntent = PendingIntent.getActivity(context, 0, null, 0)
        if (intent!!.action.equals(smsRecAction)) {
            val sb = StringBuilder()
            val bundle = intent.extras
            if (bundle != null) {
                val pdus = bundle["pdus"] as Array<*>?
                var receiverPhone = ""
                for (pdu in pdus!!) {
                    val smsMessage: SmsMessage = SmsMessage.createFromPdu(pdu as ByteArray)
                    receiverPhone = smsMessage.displayOriginatingAddress
                    sb.append("body - " + smsMessage.displayMessageBody)
                }
                val smsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(receiverPhone, null, "Odpiszę jutro.", null, null)
            }
            Toast.makeText(context, "SMS RECEIVED - $sb", Toast.LENGTH_LONG).show()

        }
    }
}
