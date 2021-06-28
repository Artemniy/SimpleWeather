package com.univer.weather.network;

import com.univer.weather.network.response.CitiesResponse;
import com.univer.weather.network.response.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("weather")
    Call<WeatherResponse> getWeatherByCity(@Query("appid") String apiKey, @Query("id") long city);
    @GET("find")
    Call<CitiesResponse> findCity(@Query("appid") String apiKey, @Query("q") String city);
}
