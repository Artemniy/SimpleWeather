package com.univer.weather;

import com.univer.weather.network.response.CitiesResponse;
import com.univer.weather.network.response.ForecastResponse;
import com.univer.weather.network.response.WeatherResponse;

public interface PresenterView {
    void onCitiesLoaded(CitiesResponse citiesResponse);

    void onWeatherLoaded(WeatherResponse weatherResponse);

    void onForecastLoaded(ForecastResponse forecastResponse);

    void onErrorLoading(String message);
}
