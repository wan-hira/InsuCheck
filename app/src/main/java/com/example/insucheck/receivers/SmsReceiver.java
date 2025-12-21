package com.example.insucheck.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // On récupère le contenu du SMS
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus != null) {
                for (Object pdu : pdus) {
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);
                    String messageBody = sms.getMessageBody();
                    String senderNumber = sms.getOriginatingAddress();

                    // Logique InsuCheck : Si le message contient "OK"
                    if (messageBody.equalsIgnoreCase("OK")) {
                        Toast.makeText(context, "Alerte confirmée par le contact !", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }
}