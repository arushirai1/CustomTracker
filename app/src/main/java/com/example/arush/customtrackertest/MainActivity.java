package com.example.arush.customtrackertest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private final int FASTEST_INTERVAL = 5000;
    private int INTERVAL;

    private GoogleApiClient client;
    private Location lastLocation;
    private LocationRequest request;
    private TextView coordinates, timestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coordinates = (TextView) findViewById(R.id.coordinate_text);
        timestamp = (TextView) findViewById(R.id.timestamp_text);
        INTERVAL = 10000;
        createLocationRequest();
        initGoogleAPIClient();
    }

    protected void onStop() {
        client.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, 123);
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(client,  request, (LocationListener) this);
        printLocationTime();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void initGoogleAPIClient() {
        if (client == null) {
            client = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(API)
                    .build();
        }
        client.connect();
    }

    protected void createLocationRequest() {
        request = new LocationRequest();
        request.setInterval(INTERVAL);
        request.setFastestInterval(FASTEST_INTERVAL);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //request.setSmallestDisplacement(10);
    }

    private void printLocationTime() {
        if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(client);

            if (lastLocation != null) {
                coordinates.setText(String.valueOf("Latitude: " + lastLocation.getLatitude())+ " Longitude: " + String.valueOf(lastLocation.getLongitude()));
                timestamp.setText(getCurrentTimeStamp());
            }
            else {
                Toast.makeText(this, "no location detected", Toast.LENGTH_LONG).show();
            }
            return;
        } else {
            Toast.makeText(this, "Permission not granted to use location services", Toast.LENGTH_LONG).show();
        }
    }

    public String getCurrentTimeStamp(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTimeStamp = dateFormat.format(new Date()); // Find todays date

            return currentTimeStamp;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        coordinates.setText(String.valueOf("Latitude: " + lastLocation.getLatitude())+ " Longitude: " + String.valueOf(lastLocation.getLongitude()));
        timestamp.setText(getCurrentTimeStamp());
    }
}
