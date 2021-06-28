package com.univer.weather.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {
    private static final String LAST_ENTERED_CITY_ID = "lastEnteredCity";
    private static SharedPreferences sharedPreferences;

    public static void initSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public static long getLastEnteredCityId() {
        return sharedPreferences.getLong(LAST_ENTERED_CITY_ID, 0);
    }

    public static void setLastEnteredCityId(long cityId) {
        sharedPreferences.edit().putLong(LAST_ENTERED_CITY_ID, cityId).apply();
    }

}
