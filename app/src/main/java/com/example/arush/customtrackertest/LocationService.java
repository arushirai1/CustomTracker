package com.example.arush.customtrackertest;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.widget.Toast;

/*import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;*/

import com.example.arush.customtrackertest.customtracker_communication.SendHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class LocationService extends Service {
    private enum State {
        IDLE, WORKING
    }

    private static State state;
    /*public static GpsCaptureService.Storage storage;

    static {
        storage = new GpsCaptureService.Storage();
    }*/

    static {
        state = State.IDLE;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (state == State.IDLE) {
            state = State.WORKING;

            if (isNetworkAvailable()) {
                while (GpsCaptureService.storage.getInUse())
                    SystemClock.sleep(1000);
                GpsCaptureService.storage.setInUse(true);
                sendToServer(GpsCaptureService.getLocationStorageInstance(this));
                GpsCaptureService.storage.clear();
                GpsCaptureService.storage.setInUse(false);
            } else {
                onSendingFinished();
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        state = State.IDLE;
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void sendToServer(Object o) {
        /*DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = myRef.child(childName);
        usersRef.setValue((HashMap)o);
        Toast.makeText(this, "sent to server", Toast.LENGTH_LONG).show();*/
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String jsonString = gson.toJson(o);
        try {
            JSONObject jsonLocations = new JSONObject(jsonString);
            SendHandler.getInstance().sendData(this, jsonLocations);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        onSendingFinished();
    }

    private void onSendingFinished() {
        this.stopSelf(); //stopSelf will call onDestroy and the WakeLock releases.
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

}

