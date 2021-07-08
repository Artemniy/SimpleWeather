package com.univer.weather.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

public class CurrentWeather {
    private long dt;
    private long sunrise;
    private long sunset;
    private float temp;
    @SerializedName("feels_like")
    private float feelsLike;
    private int pressure;
    private int humidity;
    private int visibility;
    @SerializedName("wind_speed")
    private float windSpeed;
    private ArrayList<Weather> weather;

    public long getDt() {
        return dt;
    }

    public Weather getWeather() {
        return weather.get(0);
    }

    public Date getDate() {
        return new Date(dt * 1000);
    }

    public long getSunrise() {
        return sunrise;
    }

    public long getSunset() {
        return sunset;
    }

    public int getTemp() {
        return (int) temp;
    }

    public int getFeelsLike() {
        return (int) feelsLike;
    }

    public int getPressure() {
        return pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getVisibility() {
        return visibility;
    }

    public float getWindSpeed() {
        return windSpeed;
    }
}
