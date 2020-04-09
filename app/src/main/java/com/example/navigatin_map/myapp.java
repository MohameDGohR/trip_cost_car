package com.example.navigatin_map;

import android.app.Application;

import com.beardedhen.androidbootstrap.TypefaceProvider;

public class myapp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceProvider.registerDefaultIconSets();

    }
}
