package com.univer.weather.network.response;

import com.univer.weather.model.Coordinates;
import com.univer.weather.model.MainWeatherDetails;
import com.univer.weather.model.Sys;
import com.univer.weather.model.Weather;
import com.univer.weather.model.Wind;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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

    public Coordinates getCoord() {
        return coord;
    }

    public Weather getWeather() {
        return weather.get(0);
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
                "%s, %s, %d°C", name, sys.getCountry(), (int) main.getTemp() - 273);
    }
}
