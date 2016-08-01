package com.example.arush.customtrackertest;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.arush.customtrackertest.customtracker_location.LocationTracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.annotations.Expose;
//import com.google.firebase.database.Exclude;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import static com.google.android.gms.location.LocationServices.API;

public class GpsCaptureService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static int ITERATION_COUNT = 0;
    private static int MAX_ENTRIES;
    private GoogleApiClient client;

    public static Storage storage;

    static {
        storage = new Storage();
    }
    public GpsCaptureService() {

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        MAX_ENTRIES = (int) (new Preferences(this)).getValue(Preferences.MAXIMUM_ENTRIES);
        while (storage.getInUse())
            SystemClock.sleep(1000);
        getInitGoogleAPIClient();
        return START_STICKY;
    }

    public GoogleApiClient getInitGoogleAPIClient() {
        Toast.makeText(this, "Client Connecting...", Toast.LENGTH_LONG).show();
            client = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(API)
                    .build();

        client.connect();
        return client;
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
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(this, "Client Connected", Toast.LENGTH_LONG).show();
        LocationTracker tracker = new LocationTracker(this);
        android.location.Location location = tracker.getLocation(client);
        Toast.makeText(this, "Added Location", Toast.LENGTH_LONG).show();
        storage.addLocation(location, getCurrentTimeStamp());

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public static LocationStorage getLocationStorageInstance(Context context) {
        return (new LocationStorage(context, storage));
    }

    public static class LocationStorage {
        String UserID;
        String DeviceID;
        LinkedList<Location> locations;

        public LocationStorage(Context context, Storage storage) {
            UserID = "foo";
            if (DeviceID == null)
                DeviceID = Settings.Secure.getString(context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            locations = new LinkedList<>(storage.getLocations());

        }
    }
    public static class Storage {
        LinkedList<GpsCaptureService.Location> locations;
        @Expose private boolean inUse;

        public Storage() {
            locations = new LinkedList<>();
            ITERATION_COUNT = 1;
        }


        public boolean getInUse() {
            return inUse;
        }

        //@JsonIgnore
        public void setInUse(boolean b) {
            inUse = b;
        }

        //@JsonIgnore
        public void addLocation(android.location.Location location, String timestamp){
            inUse = true;
            if(locations.size() >= MAX_ENTRIES) {
                optimizeStorage();
            }
            if (location != null)
                locations.add(new GpsCaptureService.Location(location.getLatitude(), location.getLongitude(), timestamp));
            Log.d("Storage", "Size: " + locations.size());
            inUse = false;
        }

        //@JsonIgnore
        private void optimizeStorage() {
            if (ITERATION_COUNT < MAX_ENTRIES) {
                locations.remove(ITERATION_COUNT);
                ITERATION_COUNT++;
            } else {
                ITERATION_COUNT = 1;
                locations.remove(ITERATION_COUNT);
            }

            for(int i = 0; i<locations.size(); i++) {
                Log.d("Storage Read", locations.get(i).timestamp);
            }
        }

        //@JsonIgnore
        public void clear() {
            locations.clear();
            ITERATION_COUNT = 1;
        }

        public LinkedList<Location> getLocations() {
            return locations;
        }

    }

    public void onDestroy() {
        Toast.makeText(this,"On Destroy Called", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    private static class Location {

        public double latitude;
        public double longitude;
        public String timestamp;

        public Location(double latitude, double longitude, String timestamp) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.timestamp = timestamp;
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

}
