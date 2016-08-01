package com.example.arush.customtrackertest.customtracker_location;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by arush on 7/30/2016.
 */
public class OnlineLocationTracker implements LocationListener {

    public Context context;
    public static Location lastLocation;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private FusedLocationProviderApi fusedLocationProviderApi;

    public OnlineLocationTracker(Context context, GoogleApiClient client) {
        this.context = context;
        this.client = client;
        init();

    }

    public Location getLocation() {
        if(lastLocation != null)
            Toast.makeText(context, "Online Latitude: " + lastLocation.getLatitude(), Toast.LENGTH_LONG).show();
        else {
            Toast.makeText(context, "No Location Found", Toast.LENGTH_LONG).show();
        }

        if (client != null) {
            fusedLocationProviderApi.removeLocationUpdates(client, this);
            client.disconnect();
        }

        return lastLocation;
    }

    public void init() {
        Toast.makeText(context, "Client Connected", Toast.LENGTH_LONG).show();
        initLocationRequest();
        if (!(ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(client);
            if (lastLocation != null) {
                this.lastLocation = lastLocation;
                Toast.makeText(context, "Last Location Online", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "no location detected", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1);
        locationRequest.setFastestInterval(1);
        fusedLocationProviderApi = LocationServices.FusedLocationApi;
        fusedLocationProviderApi.requestLocationUpdates(client,locationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
    }
}
