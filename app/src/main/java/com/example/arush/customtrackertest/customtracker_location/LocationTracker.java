package com.example.arush.customtrackertest.customtracker_location;

import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by arush on 7/22/2016.
 */
public class LocationTracker {
    private Context context;


    public LocationTracker(Context context) {
        this.context = context;
    }

    public android.location.Location getLocation(GoogleApiClient client) {
        android.location.Location location = null;
        if (isNetworkAvailable()) {
            Toast.makeText(context, "Network is Availiable", Toast.LENGTH_LONG).show();
            location = getOnlineLocation(client);
            if (location == null)
                location = getOfflineLocation();
        }

        else {
            location = getOfflineLocation();
        }

        return location;
    }

    private android.location.Location getOfflineLocation() {
        OfflineLocationTracker tracker = new OfflineLocationTracker(context, 1, 0);
        Log.d("customtrackertest", "Offline Mode");
        tracker.start();
        android.location.Location lastLocation = null;
        if (tracker.hasLocation()) {
            lastLocation = tracker.getLocation();
            Toast.makeText(context, "GPS Latitude: " + tracker.getLocation().getLatitude(), Toast.LENGTH_LONG).show();
        } else if (tracker.hasPossiblyStaleLocation()) {
            lastLocation = tracker.getPossiblyStaleLocation();
            Toast.makeText(context, "Stale Latitude: " + tracker.getPossiblyStaleLocation().getLatitude(), Toast.LENGTH_LONG).show();
        } else if (tracker.getNetworkLocation() != null){
            lastLocation = tracker.getNetworkLocation();
            Toast.makeText(context, "Network Latitude: " + tracker.getNetworkLocation().getLatitude(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Got no location", Toast.LENGTH_LONG).show();
        }
        tracker.stop();
        return lastLocation;
    }

    private android.location.Location getOnlineLocation(GoogleApiClient client) {
        Toast.makeText(context, "Getting Online Location...", Toast.LENGTH_LONG).show();
        OnlineLocationTracker tracker = new OnlineLocationTracker(context, client);
        Location lastLocation = tracker.getLocation();
        return lastLocation;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
