package com.example.readreceivedtext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int Request_Permission = 1;
    private static MainActivity inst;
    private Button checkPermissions;
    private Button changeScreen;

    public static MainActivity instance() {
        return inst;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inst = this;

        setup_CheckPermissionsButton();
        setup_ChangeScreenButton();
    }

    private void setup_CheckPermissionsButton()
    {
        checkPermissions = findViewById(R.id.checkPermissionsButton);
        checkPermissions.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String hasPermissions = "Has ";
                if(ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.RECEIVE_SMS}, Request_Permission);
                }
                else
                {
                    hasPermissions += "RECEIVE_SMS ";
                }
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.READ_SMS}, Request_Permission);
                }
                else
                {
                    hasPermissions += "READ_SMS";
                }
                hasPermissions += "Permissions";
                Toast.makeText(MainActivity.this, hasPermissions, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setup_ChangeScreenButton()
    {
        changeScreen = findViewById(R.id.switchScreen);
        changeScreen.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), SmsActivity.class);
            startActivity(i);
        });
    }
}