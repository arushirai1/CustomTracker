package com.example.arush.customtrackertest.customtracker_location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by arush on 7/21/2016.
 */
public class OfflineLocationTracker implements LocationListener{
    //private
    private boolean isRunning;
    private int minDistance;
    private int minTime;
    private static final String provider = LocationManager.GPS_PROVIDER;
    private static final String fallbackProvider = LocationManager.NETWORK_PROVIDER;
    private static Location lastLocation;
    private static long lastTime;
    private LocationManager locationManager;

    static {
        lastLocation = null;
        lastTime = 0;
    }

    /**
     * Initializes GPSLocationTracker using Network as a fallback
     * @param context Supply Context from Service, Activity
     * @param minTime Minimum time between intervals to receive the location
     * @param minDistance Minimum distance acts a threshold to determine whether the user has moved
     */
    public OfflineLocationTracker(Context context, int minTime, int minDistance) {
        this.minDistance = minDistance;
        this.minTime = minTime;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * Starts receiving locations
     */
    public void start() {
        if(isRunning == true)
            return;

        locationManager.requestLocationUpdates(provider, minTime, minDistance, this);
        isRunning = true;
    }

    /**
     * Stops receiving location updates
     */
    public void stop() {
        if (!isRunning)
            return;
        locationManager.removeUpdates(this);
        isRunning = false;
    }

    /**
     *
     * @return Whether we have a non-stale location
     */
     public boolean hasLocation() {
        if(lastLocation == null){
            return false;
        }
        if(System.currentTimeMillis() - lastTime > minTime && lastTime != 0){
            return false; //stale
        }
        return true;
    }

    /**
     *
     * @return Whether we have a stale location
     */
    public boolean hasPossiblyStaleLocation() {
        if(lastLocation != null)
            return false;
        return locationManager.getLastKnownLocation(provider) != null;
    }

    public Location getLocation() {
        return lastLocation;
    }

    public Location getPossiblyStaleLocation() {
        return locationManager.getLastKnownLocation(provider);
    }

    public Location getNetworkLocation() {
        locationManager.requestLocationUpdates(fallbackProvider, 0,0, this);
        Location location = locationManager.getLastKnownLocation(fallbackProvider);
        locationManager.removeUpdates(this);
        return location;
    }

    @Override
    public void onLocationChanged(Location location) {
        long now = System.currentTimeMillis();
        lastLocation = location;
        lastTime = now;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onProviderDisabled(String provider) { }
}
