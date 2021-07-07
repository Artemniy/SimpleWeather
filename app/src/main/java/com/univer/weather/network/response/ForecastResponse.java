package com.univer.weather.network.response;

import com.univer.weather.model.DailyWeather;
import com.univer.weather.model.HourlyWeather;

import java.util.ArrayList;

public class ForecastResponse extends BaseResponse {
    private ArrayList<HourlyWeather> hourly;
    private ArrayList<DailyWeather> daily;

    public ArrayList<DailyWeather> getDaily() {
        return daily;
    }

    public ArrayList<HourlyWeather> getHourly() {
        return hourly;
    }
}
