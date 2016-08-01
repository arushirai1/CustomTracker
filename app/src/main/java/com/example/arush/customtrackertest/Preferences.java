package com.example.arush.customtrackertest;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by arush on 7/16/2016.
 */
public class Preferences {
    public static enum Mode{
        OFFLINE, ONLINE;
    }
    public final static String LICENSE_PLATE_NUM = "License Plate Number";
    public final static String LOCATION_RETRIEVAL_FREQUENCY = "Interval";
    public final static String BACKGROUND_MONITER = "Background Monitor";
    public final static String MAXIMUM_ENTRIES = "Maximum Entries";
    public final static String SEND_FREQUENCY = "Send Frequency";
    public static Mode MODE;

    private final static String KEYS[] = {LICENSE_PLATE_NUM, LOCATION_RETRIEVAL_FREQUENCY, BACKGROUND_MONITER, MAXIMUM_ENTRIES, SEND_FREQUENCY};
    private final static HashMap<String, Object> DEFAULT_VALUES;
    private SharedPreferences sharedPreferences;
    private final static String PREFS = "com.example.arush.customtrackertest";

    static {
        DEFAULT_VALUES = new HashMap<>();

        DEFAULT_VALUES.put(LICENSE_PLATE_NUM, "XXXXXXXX");
        DEFAULT_VALUES.put(LOCATION_RETRIEVAL_FREQUENCY, 1.0*60*1000);
        DEFAULT_VALUES.put(BACKGROUND_MONITER, false);
        DEFAULT_VALUES.put(MAXIMUM_ENTRIES, 5);
        DEFAULT_VALUES.put(SEND_FREQUENCY, 2*60*1000);
    }

    public Preferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public Object getValue(String key) {
        if (DEFAULT_VALUES.get(key).getClass().equals(String.class))
            return sharedPreferences.getString(key, (String) DEFAULT_VALUES.get(key));
        else if (DEFAULT_VALUES.get(key).getClass().equals(Integer.class))
            return sharedPreferences.getInt(key, (Integer) DEFAULT_VALUES.get(key));
        else if (DEFAULT_VALUES.get(key).getClass().equals(Float.class))
            return sharedPreferences.getFloat(key, (Float) DEFAULT_VALUES.get(key));
        else if (DEFAULT_VALUES.get(key).getClass().equals(Boolean.class))
            return sharedPreferences.getBoolean(key, (Boolean) DEFAULT_VALUES.get(key));

        return DEFAULT_VALUES.get(key);
    }

    public void setValue(String key, Object value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (value.getClass().equals(String.class))
            editor.putString(key, (String) value);
        else if (value.getClass().equals(Integer.class))
            editor.putInt(key, (Integer) value);
        else if (value.getClass().equals(Boolean.class))
            editor.putBoolean(key, (Boolean) value);

        editor.apply();
    }

    public int getDefaultInterval() {
        return 15*60*1000;
    }
}
