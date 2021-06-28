package com.univer.weather.network.response;

import java.util.ArrayList;

public class CitiesResponse extends BaseResponse {
    private int count;
    private ArrayList<WeatherResponse> list;

    public int getCount() {
        return count;
    }

    public ArrayList<WeatherResponse> getList() {
        return list;
    }
}
