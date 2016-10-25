package ru.roscha_akademii.medialib.common;

import android.util.Log;

public class MockMediaLibApplication extends MediaLibApplication {
    public void setComponent(ApplicationComponent component) {
        this.setComponent(component);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("happy", "MockMediaLibApplication is running");
    }
}