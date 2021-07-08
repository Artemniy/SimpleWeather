package com.univer.weather.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.univer.weather.Presenter;
import com.univer.weather.PresenterView;
import com.univer.weather.R;
import com.univer.weather.model.Coordinates;
import com.univer.weather.model.DailyWeather;
import com.univer.weather.network.response.CitiesResponse;
import com.univer.weather.network.response.ForecastResponse;
import com.univer.weather.network.response.WeatherResponse;
import com.univer.weather.util.ImageUtil;
import com.univer.weather.util.NetworkStatusUtil;
import com.univer.weather.util.SharedPreferencesUtil;
import com.univer.weather.util.Weather;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, PresenterView {

    private EditText cityEt;
    private TextView city;
    private TextView wind;
    private TextView temp;
    private TextView visibility;
    private LinearLayout dailyForecastContainer;
    private TextView feelsLike;
    private SwipeRefreshLayout srLayout;
    private TextView pressure;
    private RecyclerView hourlyForecastRecycler;
    private ImageButton search;
    private TextView description;
    private TextView humidity;
    private ImageView weatherImage;
    private ImageButton about;

    private final Presenter presenter = new Presenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter.attachView(this);
        SharedPreferencesUtil.initSharedPreferences(this);
        initViews();
        srLayout.setOnRefreshListener(this);
        onRefresh();
        initCitiesSearcher();
    }

    private void initViews() {
        srLayout = findViewById(R.id.sr_layout);
        cityEt = findViewById(R.id.city_et);
        about = findViewById(R.id.about);
        about.setOnClickListener(this);
        search = findViewById(R.id.search);
        dailyForecastContainer = findViewById(R.id.daily_forecast_container);
        hourlyForecastRecycler = findViewById(R.id.hourly_forecast_recycler);
        hourlyForecastRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        hourlyForecastRecycler.addItemDecoration(new DividerItemDecoration(this, RecyclerView.HORIZONTAL));
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
        search.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == search.getId()) {
            if (!TextUtils.isEmpty(cityEt.getText())) {
                loadCitiesList();
            }
        } else if (view.getId() == about.getId()) {
            new DialogAbout().show(getSupportFragmentManager(), DialogAbout.class.getSimpleName());
        }
    }

    private void loadCitiesList() {
        if (!NetworkStatusUtil.isInternetAvailable(this)) {
            showMessage(getString(R.string.no_inet_connection));
            return;
        }

        srLayout.setRefreshing(true);
        presenter.loadCitiesList(getString(R.string.weather_api_key), cityEt.getText().toString());
    }

    private void showCitiesListDialog(ArrayList<WeatherResponse> responseArrayList) {
        String[] results = new String[responseArrayList.size()];
        for (WeatherResponse response : responseArrayList) {
            results[responseArrayList.indexOf(response)] = response.toString();
        }

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setItems(results, (dialog, which) -> {
            dialog.dismiss();
            Coordinates coord = responseArrayList.get(which).getCoord();
            SharedPreferencesUtil.setSavedCityLat((float) coord.getLat());
            SharedPreferencesUtil.setSavedCityLon((float) coord.getLon());
            SharedPreferencesUtil.setSavedCityName(String.format("%s, %s",
                    responseArrayList.get(which).getName(),
                    responseArrayList.get(which).getSys().getCountry()));
            loadCityForecast(coord.getLat(), coord.getLon());
        });
        b.show();
    }

    private void loadCityForecast(double lat, double lon) {
        if (!NetworkStatusUtil.isInternetAvailable(this)) {
            showMessage(getString(R.string.no_inet_connection));
            return;
        }

        if (lat == 0 || lon == 0) return;

        srLayout.setRefreshing(true);
        presenter.loadForecastByCoord(getString(R.string.weather_api_key), lat, lon);
    }

    private void processWeatherResult(ForecastResponse response) {
        ImageUtil.loadIcon(response.getCurrent().getWeather().getIcon(), weatherImage);
        city.setText(SharedPreferencesUtil.getSavedCityName());
        visibility.setText(String.format(Locale.getDefault(), getString(R.string.visibility), response.getCurrent().getVisibility() / 1000f));
        feelsLike.setText(String.format(getString(R.string.feels_like), response.getCurrent().getFeelsLike()));
        temp.setText(String.format(getString(R.string.celsius), response.getCurrent().getTemp()));
        humidity.setText(String.format(Locale.getDefault(), getString(R.string.humidity), response.getCurrent().getHumidity()));
        pressure.setText(String.format(Locale.getDefault(), getString(R.string.pressure), response.getCurrent().getPressure()));
        description.setText(response.getCurrent().getWeather().getDescription());
        wind.setText(String.format(Locale.getDefault(),
                "%.2fm/s", response.getCurrent().getWindSpeed()));
        setBackgroundByWeather(response.getCurrent().getWeather().getDescription());
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

    @Override
    public void onRefresh() {
        loadCityForecast(SharedPreferencesUtil.getSavedCityLat(), SharedPreferencesUtil.getSavedCityLon());
    }

    @Override
    public void onCitiesLoaded(CitiesResponse citiesResponse) {
        srLayout.setRefreshing(false);
        if (citiesResponse.getCount() == 0) {
            Toast.makeText(MainActivity.this, getText(R.string.no_results), Toast.LENGTH_SHORT).show();
        } else {
            showCitiesListDialog(citiesResponse.getList());
        }
    }

    @Override
    public void onForecastLoaded(ForecastResponse forecastResponse) {
        srLayout.setRefreshing(false);
        if (forecastResponse != null) {
            processWeatherResult(forecastResponse);
            srLayout.setRefreshing(false);

            SharedPreferencesUtil.setSavedCityLat((float) forecastResponse.getLat());
            SharedPreferencesUtil.setSavedCityLon((float) forecastResponse.getLon());

            hourlyForecastRecycler.setAdapter(new HourlyForecastAdapter(forecastResponse.getHourly()));
            dailyForecastContainer.removeAllViews();
            for (DailyWeather forecast : forecastResponse.getDaily()) {
                addDailyForecast(forecast);
            }
        } else {
            onErrorLoading(getString(R.string.something_went_wrong));
        }
    }

    private void addDailyForecast(DailyWeather forecast) {
        View view = getLayoutInflater().inflate(R.layout.item_daily_forecast, dailyForecastContainer, false);
        TextView weekDay = view.findViewById(R.id.week_day);
        ImageView icon = view.findViewById(R.id.weather_icon);
        TextView temp = view.findViewById(R.id.temp);
        temp.setText(String.format(getString(R.string.celsius), forecast.getTemp()));
        weekDay.setText(forecast.getWeekDay());
        ImageUtil.loadIcon(forecast.getWeather().get(0).getIcon(), icon);
        dailyForecastContainer.addView(view);
    }

    @Override
    public void onErrorLoading(String message) {
        srLayout.setRefreshing(false);
        showMessage(message);
    }

}