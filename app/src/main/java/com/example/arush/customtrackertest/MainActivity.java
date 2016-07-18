package com.example.arush.customtrackertest;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.google.android.gms.location.LocationServices.*;

public class MainActivity extends AppCompatActivity {
    public static TextView coordinates, timestamp;
    private Switch enabler_switch;
    private LocationReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        coordinates = (TextView) findViewById(R.id.coordinate_text);
        timestamp = (TextView) findViewById(R.id.timestamp_text);
        receiver = new LocationReceiver();
        final Preferences prefs = new Preferences(this);

        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, 123);
        }

        enabler_switch = (Switch) findViewById(R.id.enabler_switch);
        enabler_switch.setChecked((Boolean) prefs.getValue(Preferences.BACKGROUND_MONITER));
        enabler_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefs.setValue(Preferences.BACKGROUND_MONITER, isChecked);

                if (isChecked) {
                    PackageManager pm = MainActivity.this.getPackageManager();
                    ComponentName componentName = new ComponentName(MainActivity.this, LocationReceiver.class);
                    pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP);
                    Toast.makeText(getApplicationContext(), "activated", Toast.LENGTH_LONG).show();

                    Intent filter = new Intent();
                    receiver.onReceive(getApplicationContext(), filter);
                } else {
                    PackageManager pm = MainActivity.this.getPackageManager();
                    ComponentName componentName = new ComponentName(MainActivity.this, LocationReceiver.class);
                    pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP);
                    receiver.cancelAlarm();
                    Toast.makeText(getApplicationContext(), "cancelled", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void broadcastIntent() {
        Toast.makeText(this, "test", Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.setAction("com.example.arush.customtrackertest.LocationReciever");
        sendBroadcast(intent);
    }

}