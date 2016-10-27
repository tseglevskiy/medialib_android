package ru.roscha_akademii.medialib.common;

import android.util.Log;

public class MockMediaLibApplication extends MediaLibApplication {
    public void setTestComponent(ApplicationComponent component) {
        this.setTestComponent(component);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("happy", "MockMediaLibApplication is running");
    }
}