package com.univer.weather.util;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageUtil {
    private static final String IMAGE_URL = "https://openweathermap.org/img/w/";
    private static final String PNG = ".png";

    public static void loadIcon(String icon, ImageView imageView) {
        Picasso.get().load(IMAGE_URL + icon + PNG)
                .into(imageView);
    }
}
