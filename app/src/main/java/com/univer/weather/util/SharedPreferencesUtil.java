package com.univer.weather.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {
    private static final String LONDON = "London, GB";

    private static final String LAST_ENTERED_CITY_ID = "lastEnteredCity";
    private static final String SAVED_CITY_LAT = "savedCityLat";
    private static final String SAVED_CITY_LNG = "savedCityLng";
    private static final String SAVED_CITY_NAME = "savedCityName";
    private static SharedPreferences sharedPreferences;

    public static void initSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public static void setSavedCityLat(float savedCityLat) {
        sharedPreferences.edit().putFloat(SAVED_CITY_LAT, savedCityLat).apply();
    }

    public static float getSavedCityLat() {
        return sharedPreferences.getFloat(SAVED_CITY_LAT, 51.50986f);
    }

    public static void setSavedCityName(String name) {
        sharedPreferences.edit().putString(SAVED_CITY_NAME, name).apply();
    }

    public static String getSavedCityName() {
        return sharedPreferences.getString(SAVED_CITY_NAME, LONDON);
    }

    public static void setSavedCityLon(float savedCityLon) {
        sharedPreferences.edit().putFloat(SAVED_CITY_LNG, savedCityLon).apply();
    }

    public static float getSavedCityLon() {
        return sharedPreferences.getFloat(SAVED_CITY_LNG, -0.118092f);
    }
}
