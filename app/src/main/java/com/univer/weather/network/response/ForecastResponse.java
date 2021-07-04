package com.univer.weather.network.response;

import java.util.ArrayList;

public class ForecastResponse extends BaseResponse {
    private ArrayList<WeatherResponse> list;

    public ArrayList<WeatherResponse> getList() {
        return list;
    }
}
