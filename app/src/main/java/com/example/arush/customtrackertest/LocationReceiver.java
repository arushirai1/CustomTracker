package com.example.arush.customtrackertest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.widget.Toast;

public class LocationReceiver extends BroadcastReceiver {
    AlarmManager alarmManager_LS, alarmManager_GCS;
    PendingIntent locationServiceIntent, gpsCaptureServiceIntent;
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        //TODO: trigger onReceive when rebooting device
        Preferences prefs = new Preferences(context);
        this.context = context;
        Toast.makeText(context, "activated broadband receiver", Toast.LENGTH_LONG).show();
        long retrieval_freq = new Double((Double )prefs.getValue(Preferences.LOCATION_RETRIEVAL_FREQUENCY)).longValue();
        setGpsCaptureServiceAlarm(context, retrieval_freq);
        setLocationServiceAlarm(context, (Integer) prefs.getValue(Preferences.SEND_FREQUENCY));
    }

    public void setLocationServiceAlarm(Context context, int interval) {
        alarmManager_LS = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        locationServiceIntent = PendingIntent.getService(context, 0,
                new Intent(context, LocationService.class), PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager_LS.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + interval,
                interval, locationServiceIntent);

        //just for testing
        Intent serviceIntent = new Intent(context, LocationService.class);
        context.startService(serviceIntent);
    }

    public void setGpsCaptureServiceAlarm(Context context, long interval) {
        alarmManager_GCS = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        gpsCaptureServiceIntent = PendingIntent.getService(context, 0,
                new Intent(context, GpsCaptureService.class), PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager_GCS.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + interval,
                interval, gpsCaptureServiceIntent);

        //just for testing
        Intent serviceIntent = new Intent(context, GpsCaptureService.class);
        context.startService(serviceIntent);
    }

    public void cancelAlarm_LS() {
        if (alarmManager_LS!= null) {
            Toast.makeText(context, "cancel Location Service alarm", Toast.LENGTH_LONG).show();
            alarmManager_LS.cancel(locationServiceIntent);
        }
    }

    public void cancelAlarm_GCS() {
        if (alarmManager_GCS!= null) {
            Toast.makeText(context, "cancel GPS Capture Service alarm", Toast.LENGTH_LONG).show();
            alarmManager_GCS.cancel(gpsCaptureServiceIntent);
        }
    }
}

