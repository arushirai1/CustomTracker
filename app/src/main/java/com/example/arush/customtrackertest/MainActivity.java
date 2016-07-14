package com.example.arush.customtrackertest;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.google.android.gms.location.LocationServices.*;

public class MainActivity extends AppCompatActivity {
    private static TextView coordinates, timestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coordinates = (TextView) findViewById(R.id.coordinate_text);
        timestamp = (TextView) findViewById(R.id.timestamp_text);

        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, 123);
        }

        PackageManager pm = this.getPackageManager();
        ComponentName receiver = new ComponentName(this, LocationReceiver.class);
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        Toast.makeText(this, "on create", Toast.LENGTH_LONG).show();
    }

    public void broadcastIntent(){
        Toast.makeText(this, "test", Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.setAction("com.example.arush.customtrackertest.LocationReciever");
        sendBroadcast(intent);
    }
}
