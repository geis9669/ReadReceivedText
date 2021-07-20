package com.example.readreceivedtext;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

public class SmsReceiver extends BroadcastReceiver {
    public static final String SMS_BUNDLE = "pdus";
    public TextToSpeech ttobj;

    @Override
    public void onReceive(Context context, Intent intent) {
        // This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

//        Toast.makeText(context, "Start OnReceive", Toast.LENGTH_LONG).show();

        Bundle intentExtras = intent.getExtras();
        if(intentExtras != null){
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            for(int i=0; i<sms.length; i++){
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);
                String smsBody = smsMessage.getMessageBody().toString();
                String address = smsMessage.getOriginatingAddress();
                smsMessageStr += "SMS From: " + address + "\n";
                smsMessageStr += smsBody + "\n";
            }
            Toast.makeText(context, smsMessageStr, Toast.LENGTH_SHORT).show();

            //this will update the UI with message
            SmsActivity inst = SmsActivity.instance();
            inst.updateList(smsMessageStr);

            ttobj = new TextToSpeech(MainActivity.instance().getApplicationContext(), new TextToSpeech.OnInitListener() {
                public void onInit(int status)
                {
                    if(status != TextToSpeech.ERROR)
                    {
                        ttobj.setLanguage(Locale.US);
                    }
                }
            });
            ttobj.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String utteranceId) {

                }

                @Override
                public void onDone(String utteranceId) {
                    ttobj.stop();
                    ttobj.shutdown();
                }

                @Override
                public void onError(String utteranceId) {
                    ttobj.stop();
                    ttobj.shutdown();
                }
            });

            ttobj.speak(smsMessageStr, TextToSpeech.QUEUE_FLUSH, null);


        }


        // Get the SMS message
//        Bundle bundle = intent.getExtras();
//        SmsMessage[] msgs;
//        String strMessage = "";
//        String format = bundle.getString("format");
//        // Retrieve the SMS message received.
//        Object[] pdus = (Object[]) bundle.get("pdus");
//        if(pdus != null) {
//            // Check the Android version.
//            boolean isVersionM = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
//            //Fill the msgs array.
//            msgs = new SmsMessage[pdus.length];
//            for(int i = 0; i<msgs.length; i++){
//                // Check Android version and use appropriate create From Pdu.
//                if(isVersionM)
//                {
//                    //If Android version M or newer:
//                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
//                } else {
//                    // If Android version L or older:
//                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
//                }
//                // Build the message to show.
//                strMessage += "SMS from " + msgs[i].getOriginatingAddress();
//                strMessage += " :" + msgs[i].getMessageBody() +"\n";
//                // Log and display the SMS message.
//                Log.d("ReadReceivedText", "onReceive: " + strMessage);
//                Toast.makeText(context, strMessage, Toast.LENGTH_LONG).show();
//            }
//        }

    }
}