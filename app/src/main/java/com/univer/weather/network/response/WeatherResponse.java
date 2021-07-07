package com.univer.weather.network.response;

import com.google.gson.annotations.SerializedName;
import com.univer.weather.model.Coordinates;
import com.univer.weather.model.MainWeatherDetails;
import com.univer.weather.model.Sys;
import com.univer.weather.model.Weather;
import com.univer.weather.model.Wind;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WeatherResponse extends BaseResponse {
    private Coordinates coord;
    private ArrayList<Weather> weather;
    private MainWeatherDetails main;
    private String name;
    private Wind wind;
    private Sys sys;
    private int visibility;
    private int id;
    private float temp;
    @SerializedName("dt_txt")
    private String date;

    public Coordinates getCoord() {
        return coord;
    }

    public String getDate() {
        return date;
    }

    public float getTemp() {
        return temp;
    }

    public boolean isNow() {
        if (getDateObj() == null) return false;
        return getDateObj().getTime() <= new Date().getTime();
    }

    public Weather getWeather() {
        return weather.get(0);
    }

    public String getWeekDay() {
        if (getDateObj() == null) return "";
        return new SimpleDateFormat("EEEE", Locale.ENGLISH).format(getDateObj());
    }

    public Date getDateObj() {
        if (date == null) return null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isMiddleOfDay() {
        if (getDateObj() == null) return false;
        Calendar c = Calendar.getInstance();
        c.setTime(getDateObj());
        return c.get(Calendar.HOUR_OF_DAY) == 12;
    }

    public boolean isToday() {
        if (getDateObj() == null) return false;
        Calendar then = Calendar.getInstance();
        then.setTime(getDateObj());
        Calendar today = Calendar.getInstance();
        return then.get(Calendar.YEAR) == today.get(Calendar.YEAR)
                && then.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH);
    }

    public String getTime() {
        if (getDateObj() == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("HH", Locale.getDefault());
        return sdf.format(getDateObj());
    }

    public MainWeatherDetails getMain() {
        return main;
    }

    public String getName() {
        return name;
    }

    public Wind getWind() {
        return wind;
    }

    public int getId() {
        return id;
    }

    public Sys getSys() {
        return sys;
    }

    public int getVisibility() {
        return visibility;
    }

    @NotNull
    @Override
    public String toString() {
        return String.format(Locale.getDefault(),
                "%s, %s, %d°C", name, sys.getCountry(), (int) main.getTemp());
    }
}
