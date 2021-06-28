package com.univer.weather;

import java.util.concurrent.Callable;

public interface WeatherInterface<R> extends Callable<R> {
    void onDataLoaded(R result);

    void onDataLoading();
}
