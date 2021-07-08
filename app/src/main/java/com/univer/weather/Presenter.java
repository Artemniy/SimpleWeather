package com.univer.weather;

import com.univer.weather.network.ApiClient;
import com.univer.weather.network.response.CitiesResponse;
import com.univer.weather.network.response.ForecastResponse;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Presenter {
    private PresenterView presenterView;

    public void attachView(PresenterView presenterView) {
        this.presenterView = presenterView;
    }

    public void loadForecastByCoord(String apiKey, double lat, double lon) {
        ApiClient.getApiService().getForecast(apiKey, lat, lon)
                .enqueue(new Callback<ForecastResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<ForecastResponse> call, @NotNull Response<ForecastResponse> response) {
                        ForecastResponse forecastResponse = response.body();
                        presenterView.onForecastLoaded(forecastResponse);
                    }

                    @Override
                    public void onFailure(@NotNull Call<ForecastResponse> call, @NotNull Throwable t) {
                        t.printStackTrace();
                        presenterView.onErrorLoading(App.getInstance().getString(R.string.something_went_wrong));
                    }
                });
    }


    public void loadCitiesList(String apiKey, String city) {
        ApiClient.getApiService().findCity(apiKey, city).enqueue(new Callback<CitiesResponse>() {
            @Override
            public void onResponse(@NotNull Call<CitiesResponse> call, @NotNull Response<CitiesResponse> response) {
                CitiesResponse citiesResponse = response.body();
                presenterView.onCitiesLoaded(citiesResponse);
            }

            @Override
            public void onFailure(@NotNull Call<CitiesResponse> call, @NotNull Throwable t) {
                t.printStackTrace();
                presenterView.onErrorLoading(App.getInstance().getString(R.string.something_went_wrong));
            }
        });

    }
}
