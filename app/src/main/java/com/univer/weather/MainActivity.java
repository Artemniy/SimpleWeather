package com.univer.weather;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.univer.weather.network.ApiClient;
import com.univer.weather.network.response.CitiesResponse;
import com.univer.weather.network.response.WeatherResponse;
import com.univer.weather.util.ImageUtil;
import com.univer.weather.util.NetworkStatusUtil;
import com.univer.weather.util.SharedPreferencesUtil;
import com.univer.weather.util.Weather;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final int LONDON_ID = 2643743;

    private EditText cityEt;
    private TextView city;
    private TextView wind;
    private TextView temp;
    private TextView visibility;
    private TextView feelsLike;
    private SwipeRefreshLayout srLayout;
    private TextView pressure;
    private ImageButton search;
    private TextView description;
    private TextView humidity;
    private ImageView weatherImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferencesUtil.initSharedPreferences(this);
        initViews();
        srLayout.setOnRefreshListener(this);
        loadWeatherByCityId(SharedPreferencesUtil.getLastEnteredCityId() == 0
                ? LONDON_ID : SharedPreferencesUtil.getLastEnteredCityId());
        initCitiesSearcher();
    }

    private void initViews() {
        srLayout = findViewById(R.id.sr_layout);
        cityEt = findViewById(R.id.city_et);
        search = findViewById(R.id.search);
        city = findViewById(R.id.city);
        wind = findViewById(R.id.wind);
        visibility = findViewById(R.id.visibility);
        temp = findViewById(R.id.temp);
        description = findViewById(R.id.description);
        feelsLike = findViewById(R.id.feels_like);
        pressure = findViewById(R.id.pressure);
        weatherImage = findViewById(R.id.icon);
        humidity = findViewById(R.id.humidity);
    }

    private void initCitiesSearcher() {
        search.setOnClickListener(view -> {
            if (TextUtils.isEmpty(cityEt.getText())) return;
            loadCitiesList();
        });
    }

    private void loadCitiesList() {
        if (!NetworkStatusUtil.isInternetAvailable(this)) {
            showMessage(getString(R.string.no_inet_connection));
            return;
        }

        srLayout.setRefreshing(true);
        ApiClient.getApiService().findCity(getString(R.string.weather_api_key),
                cityEt.getText().toString()).enqueue(new Callback<CitiesResponse>() {
            @Override
            public void onResponse(@NotNull Call<CitiesResponse> call, @NotNull Response<CitiesResponse> response) {
                srLayout.setRefreshing(false);
                CitiesResponse citiesResponse = response.body();
                if (citiesResponse != null && citiesResponse.isSuccess()) {
                    if (citiesResponse.getCount() == 0) {
                        Toast.makeText(MainActivity.this, getText(R.string.no_results), Toast.LENGTH_SHORT).show();
                    } else {
                        showCitiesListDialog(citiesResponse.getList());
                    }
                } else {
                    showMessage(citiesResponse == null ? getString(R.string.no_results) : citiesResponse.getMessage());
                }
            }

            @Override
            public void onFailure(@NotNull Call<CitiesResponse> call, @NotNull Throwable t) {
                srLayout.setRefreshing(false);
                t.printStackTrace();
                showMessage(getString(R.string.something_went_wrong));
            }
        });
    }

    private void showCitiesListDialog(ArrayList<WeatherResponse> responseArrayList) {
        String[] results = new String[responseArrayList.size()];
        for (WeatherResponse response : responseArrayList) {
            results[responseArrayList.indexOf(response)] = response.toString();
        }

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setItems(results, (dialog, which) -> {
            dialog.dismiss();
            loadWeatherByCityId(responseArrayList.get(which).getId());
        });
        b.show();
    }

    private void loadWeatherByCityId(long cityId) {
        if (!NetworkStatusUtil.isInternetAvailable(this)) {
            showMessage(getString(R.string.no_inet_connection));
            return;
        }

        if (cityId == 0) return;
        srLayout.setRefreshing(true);
        ApiClient.getApiService().getWeatherByCity(getString(R.string.weather_api_key), cityId)
                .enqueue(new Callback<WeatherResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<WeatherResponse> call, @NotNull Response<WeatherResponse> response) {
                        WeatherResponse weatherResponse = response.body();
                        if (weatherResponse != null && weatherResponse.isSuccess()) {
                            SharedPreferencesUtil.setLastEnteredCityId(weatherResponse.getId());
                            if (weatherResponse == null) return;
                            processResult(weatherResponse);
                            srLayout.setRefreshing(false);
                        } else {
                            showMessage(weatherResponse == null ? getString(R.string.something_went_wrong) : weatherResponse.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<WeatherResponse> call, @NotNull Throwable t) {
                        t.printStackTrace();
                        srLayout.setRefreshing(false);
                        showMessage(getString(R.string.something_went_wrong));
                    }
                });
    }

    private void processResult(WeatherResponse weatherResponse) {
        ImageUtil.loadIcon(weatherResponse.getWeather().getIcon(), weatherImage);
        city.setText(String.format("%s, %s", weatherResponse.getName(), weatherResponse.getSys().getCountry()));
        visibility.setText(String.format(Locale.getDefault(), getString(R.string.visibility), weatherResponse.getVisibility() / 1000f));
        feelsLike.setText(String.format(getString(R.string.feels_like), convertFahrenheitToCelcius(weatherResponse.getMain().getFeelsLike())));
        temp.setText(String.format(getString(R.string.celsius), convertFahrenheitToCelcius(weatherResponse.getMain().getTemp())));
        humidity.setText(String.format(Locale.getDefault(), getString(R.string.humidity), weatherResponse.getMain().getHumidity()));
        pressure.setText(String.format(Locale.getDefault(), getString(R.string.pressure), weatherResponse.getMain().getPressure()));
        description.setText(weatherResponse.getWeather().getDescription());
        wind.setText(String.format(Locale.getDefault(),
                "%.2fm/s", weatherResponse.getWind().getSpeed()));
        setBackgroundByWeather(weatherResponse.getWeather().getDescription());
    }

    private void setBackgroundByWeather(String description) {
        switch (description) {
            case Weather
                    .CLEAR_SKY:
                srLayout.setBackgroundResource(R.drawable.clear_sky);
                break;
            case Weather.FEW_CLOUDS:
                srLayout.setBackgroundResource(R.drawable.sky_bg);
                break;
            case Weather.SNOW:
                srLayout.setBackgroundResource(R.drawable.snow_bg);
                break;
            case Weather.THUNDERSTORM:
                srLayout.setBackgroundResource(R.drawable.thunder_bg);
                break;
            case Weather.RAIN:
            case Weather.SHOWER_RAIN:
            case Weather.LIGHT_RAIN:
                srLayout.setBackgroundResource(R.drawable.rain_bg);
                break;
            default:
                srLayout.setBackgroundResource(R.drawable.clouds_bg);
                break;
        }
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private String convertFahrenheitToCelcius(float fahrenheit) {
        return String.valueOf((int) (fahrenheit - 273));
    }

    @Override
    public void onRefresh() {
        loadWeatherByCityId(SharedPreferencesUtil.getLastEnteredCityId());
    }
}