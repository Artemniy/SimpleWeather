package com.univer.weather.network.response;

public abstract class BaseResponse {
    private static final int CODE_SUCCESS = 200;
    private String message;
    private int cod;

    public String getMessage() {
        return message;
    }

    public int getCod() {
        return cod;
    }

    public boolean isSuccess() {
        return cod == CODE_SUCCESS;
    }
}
