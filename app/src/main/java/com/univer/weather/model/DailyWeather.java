package com.univer.weather.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DailyWeather {
    private Temp temp;
    private ArrayList<Weather> weather;
    private long dt;

    public int getTemp() {
        return (int) temp.getDay();
    }

    public String getWeekDay() {
        if (getDate() == null) return "";
        return new SimpleDateFormat("EEEE", Locale.ENGLISH).format(getDate());
    }

    public ArrayList<Weather> getWeather() {
        return weather;
    }

    public Date getDate() {
        return new Date(dt * 1000);
    }
}
