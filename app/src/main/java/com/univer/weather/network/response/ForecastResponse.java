package com.univer.weather.network.response;

import com.univer.weather.model.CurrentWeather;
import com.univer.weather.model.DailyWeather;
import com.univer.weather.model.HourlyWeather;

import java.util.ArrayList;

public class ForecastResponse extends BaseResponse {
    private double lat;
    private double lon;
    private CurrentWeather current;
    private ArrayList<HourlyWeather> hourly;
    private ArrayList<DailyWeather> daily;

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public CurrentWeather getCurrent() {
        return current;
    }

    public ArrayList<DailyWeather> getDaily() {
        return daily;
    }

    public ArrayList<HourlyWeather> getHourly() {
        return hourly;
    }
}
