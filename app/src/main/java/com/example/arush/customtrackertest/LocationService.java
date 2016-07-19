package com.example.arush.customtrackertest;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import static com.google.android.gms.location.LocationServices.API;

public class LocationService extends Service implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private enum State{
        IDLE,WORKING;
    }
    private static State state;
    private final int FASTEST_INTERVAL = 5000;
    private int INTERVAL;

    private GoogleApiClient client;
    private static PriorityQueue<Location> storage;
    private LocationRequest request;
    private FirebaseDatabase database;
    private Map<String, User> users;

    static {
        state = State.IDLE;
        storage = new PriorityQueue<>();
    }

    private static class User{
        double lastLat;
        double lastLong;
        String timestamp;

        public User(double lat, double longitude, String timestamp) {
            lastLat = lat;
            lastLong = longitude;
            this.timestamp = timestamp;
        }
    };

    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
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
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(client);

            if (lastLocation != null) {
                Preferences prefs = new Preferences(getApplicationContext());
                users.put((String) prefs.getValue(prefs.LICENSE_PLATE_NUM), new User(lastLocation.getLatitude(), lastLocation.getLongitude(), getCurrentTimeStamp()));
                sendToServer();
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
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (state == State.IDLE) {
            state = State.WORKING;
            INTERVAL = 10000;
            createLocationRequest();
            initGoogleAPIClient();
            users = new HashMap<>();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        state = State.IDLE;
        super.onDestroy();
    }

    private void sendToServer() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = myRef.child("Users");
        usersRef.setValue(users);
        Toast.makeText(this, "sent to server", Toast.LENGTH_LONG).show();
        onSendingFinished();
    }

    private void onSendingFinished() {
        this.stopSelf(); //stopSelf will call onDestroy and the WakeLock releases.
    }

}

