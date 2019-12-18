package com.khan.broadcastsms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText phoneNumber, msg;
    Button sendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Requesting Run Time Permissions
        String[] appPermissions = new String[]{Manifest.permission.SEND_SMS};
        ActivityCompat.requestPermissions(this, appPermissions, 1);

        // XML Attachments
        phoneNumber = findViewById(R.id.phoneNum);
        msg = findViewById(R.id.msg);
        sendBtn = findViewById(R.id.sendBtn);

        // Setting onClick Listener on SendBTN
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(phoneNumber.getText().toString(), msg.getText().toString());
            }
        });
    }

    public void sendMessage(String phoneNumber, String msg) {
        if (!(phoneNumber.isEmpty()) && !(msg.isEmpty()))
            try {
                SmsManager sms = SmsManager.getDefault();
                PendingIntent abc = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT"), 0);
                sms.sendTextMessage(phoneNumber, null, msg, abc, null);

                registerReceiver(new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        int r = getResultCode();
                        String res = r == 1 ? "Generic Failure" : r == 2 ? "Radio off" : r == 3 ? "Null PDU" : r == 4 ? "No Service" : null;
                        Toast.makeText(getBaseContext(), "SMS " + (r == -1 ? "SENT !" : "not Sent" + ((res != null) ? ": " + res : null)), Toast.LENGTH_SHORT).show();
                    }
                }, new IntentFilter("SMS_SENT"));

            } catch (Exception e) { e.printStackTrace(); }
        else
            Toast.makeText(this, "Please Enter " + (phoneNumber.isEmpty() ? "Phone Number" : "Your Message"), Toast.LENGTH_SHORT).show();
    }

}



















