package com.univer.weather.network;

import com.univer.weather.network.response.CitiesResponse;
import com.univer.weather.network.response.ForecastResponse;
import com.univer.weather.network.response.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("weather?units=metric")
    Call<WeatherResponse> getWeatherByCity(@Query("appid") String apiKey, @Query("id") long cityId);

    @GET("find?units=metric")
    Call<CitiesResponse> findCity(@Query("appid") String apiKey, @Query("q") String city);

    /*@GET("forecast?units=metric")
    Call<ForecastResponse> getForecast(@Query("appid") String apiKey, @Query("id") long cityId);
*/
    @GET("onecall?units=metric&exclude=minutely")
    Call<ForecastResponse> getForecast(@Query("appid") String apiKey, @Query("lat") double lat, @Query("lon") double lon);
}
