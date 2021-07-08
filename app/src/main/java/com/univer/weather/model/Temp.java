package com.univer.weather.model;

public class Temp {
    private float day;
    private float min;
    private float max;
    private float night;
    private float eve;
    private float morn;

    public int getMin() {
        return (int) min;
    }

    public int getMax() {
        return (int) max;
    }

    public float getDay() {
        return day;
    }
}
