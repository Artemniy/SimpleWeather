package com.univer.weather.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HourlyWeather {
    private float temp;
    private ArrayList<Weather> weather;
    private long dt;

    public int getTemp() {
        return (int) temp;
    }

    public boolean isNow() {
        if (getDate() == null) return false;
        return getDate().getTime() <= new Date().getTime();
    }

    public String getTime() {
        if (getDate() == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("HH", Locale.getDefault());
        return sdf.format(getDate());
    }

    public ArrayList<Weather> getWeather() {
        return weather;
    }

    public Date getDate() {
        return new Date(dt * 1000);
    }
}
