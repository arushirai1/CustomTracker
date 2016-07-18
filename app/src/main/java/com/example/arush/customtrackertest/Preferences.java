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
    public final static String INTERVAL = "Interval";
    public final static String BACKGROUND_MONITER = "Background Monitor";
    public final static String MAXIMUM_ENTRIES = "Maximum Entries";
    public static Mode MODE;

    private final static String KEYS[] = {LICENSE_PLATE_NUM, INTERVAL, BACKGROUND_MONITER, MAXIMUM_ENTRIES};
    private final static HashMap<String, Object> DEFAULT_VALUES;
    private SharedPreferences sharedPreferences;
    private final static String PREFS = "com.example.arush.customtrackertest";

    static {
        DEFAULT_VALUES = new HashMap<>();

        DEFAULT_VALUES.put(LICENSE_PLATE_NUM, "XX-XXXXXX");
        DEFAULT_VALUES.put(INTERVAL,1*60*1000);
        DEFAULT_VALUES.put(BACKGROUND_MONITER, false);
        DEFAULT_VALUES.put(MAXIMUM_ENTRIES, 5);
    }

    public Preferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public Object getValue(String key) {
        if (DEFAULT_VALUES.get(key).getClass().equals(String.class))
            return sharedPreferences.getString(key, (String) DEFAULT_VALUES.get(key));
        else if (DEFAULT_VALUES.get(key).getClass().equals(Integer.class))
            return sharedPreferences.getInt(key, (Integer) DEFAULT_VALUES.get(key));
        else if (DEFAULT_VALUES.get(key).getClass().equals(Boolean.class))
            return sharedPreferences.getBoolean(key, (Boolean) DEFAULT_VALUES.get(key));

        return null;
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
}
