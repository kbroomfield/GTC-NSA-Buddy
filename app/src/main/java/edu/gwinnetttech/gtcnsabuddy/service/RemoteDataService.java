package edu.gwinnetttech.gtcnsabuddy.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by Kyle on 11/16/2016.
 *
 * A singleton class to make HTTP requests and load images.
 *
 */

public class RemoteDataService {
    private static RemoteDataService INSTANCE;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    private RemoteDataService(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    public static synchronized RemoteDataService getInstance(Context context) {
        if ( INSTANCE == null ) {
            INSTANCE = new RemoteDataService(context);
        }

        return INSTANCE;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}
