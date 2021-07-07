package com.univer.weather.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.univer.weather.R;
import com.univer.weather.model.HourlyWeather;
import com.univer.weather.network.response.WeatherResponse;
import com.univer.weather.util.ImageUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HourlyForecastAdapter extends RecyclerView.Adapter<HourlyForecastAdapter.ViewHolder> {
    private ArrayList<HourlyWeather> forecasts;

    public HourlyForecastAdapter(ArrayList<HourlyWeather> forecasts) {
        this.forecasts = forecasts;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hourly_forecast, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        HourlyWeather forecast = forecasts.get(position);
        holder.temp.setText(String.format(holder.itemView.getContext().getString(R.string.celsius),
                forecast.getTemp()));
        ImageUtil.loadIcon(forecast.getWeather().get(0).getIcon(), holder.icon);
        holder.time.setText(forecast.isNow() ? holder.itemView.getContext().getString(R.string.now)
                : forecast.getTime());
    }

    @Override
    public int getItemCount() {
        return forecasts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView temp;
        private final TextView time;
        private final ImageView icon;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            temp = itemView.findViewById(R.id.temp);
            time = itemView.findViewById(R.id.time);
            icon = itemView.findViewById(R.id.weather_icon);
        }
    }
}
