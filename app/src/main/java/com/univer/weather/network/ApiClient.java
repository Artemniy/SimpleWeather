package com.univer.weather.network;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String API_CLIENT_TAG = "API_CLIENT_TAG";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";


    private static Retrofit retrofit = buildClient();
    private static ApiInterface apiService;

    private static Retrofit buildClient() {
        if (retrofit == null) {
            OkHttpClient client =
                    new OkHttpClient
                            .Builder()
                            .readTimeout(60, TimeUnit.SECONDS)
                            .connectTimeout(60, TimeUnit.SECONDS)
                            .writeTimeout(60, TimeUnit.SECONDS)
                            .retryOnConnectionFailure(true)
                            .addInterceptor(new MyInterceptor())
                            .build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static ApiInterface getApiService() {
        if (apiService == null) {
            apiService = retrofit.create(ApiInterface.class);
        }
        return apiService;
    }

    static class MyInterceptor implements Interceptor {

        @NonNull
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            Log.d(API_CLIENT_TAG,
                    String.format("Sending request %s on %n%s", request.url(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();

            Log.d(API_CLIENT_TAG,
                    String.format("Received response for %s in %.1fms%n%s", response.request().url(),
                            (t2 - t1) / 1e6d, response.headers()));
                return response;
        }
    }

}
