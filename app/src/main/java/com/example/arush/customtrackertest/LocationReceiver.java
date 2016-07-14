package com.example.arush.customtrackertest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.SystemClock;

public class LocationReceiver extends BroadcastReceiver {
    private int INTERVAL;
    public LocationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        INTERVAL = 2*60*1000;
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final PendingIntent wakeupIntent = PendingIntent.getService(context, 0,
                new Intent(context, LocationService.class), PendingIntent.FLAG_UPDATE_CURRENT);

        final boolean hasNetwork = !intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
        if (hasNetwork) {
            // start service now for doing once
            context.startService(new Intent(context, LocationService.class));

            // schedule service for every 15 minutes
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + INTERVAL,
                    INTERVAL, wakeupIntent);
        } else {
            alarmManager.cancel(wakeupIntent);
        }
    }
}
