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
    private int INTERVAL;
    AlarmManager alarmManager;
    PendingIntent wakeupIntent;
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        Preferences prefs = new Preferences(context);
        INTERVAL = (Integer) prefs.getValue(Preferences.INTERVAL);
        this.context = context;
        Toast.makeText(context, "activated broadband receiver", Toast.LENGTH_LONG).show();

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        wakeupIntent = PendingIntent.getService(context, 0,
                new Intent(context, LocationService.class), PendingIntent.FLAG_UPDATE_CURRENT);

        final boolean hasNetwork = !intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
        if (hasNetwork) {

            // start service now for doing once
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + INTERVAL,
                    INTERVAL, wakeupIntent);
            Intent serviceIntent = new Intent(context, LocationService.class);
            context.startService(serviceIntent);

            // schedule service for every 15 minutes
        } else {
            alarmManager.cancel(wakeupIntent);
        }

    }

    public void cancelAlarm() {
        if (alarmManager!= null) {
            Toast.makeText(context, "cancel alarms", Toast.LENGTH_LONG).show();
            alarmManager.cancel(wakeupIntent);
        }
    }
}

