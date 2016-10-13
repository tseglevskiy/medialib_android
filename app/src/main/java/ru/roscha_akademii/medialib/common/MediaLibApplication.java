package ru.roscha_akademii.medialib.common;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import io.fabric.sdk.android.Fabric;

public class MediaLibApplication extends Application {
    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        refWatcher = LeakCanary.install(this);
        Fabric.with(this, new Crashlytics());
    }

    protected ApplicationComponent component;

    private ApplicationComponent createComponent() {
        return DaggerApplicationComponent
                .builder()
                .androidModule(new AndroidModule(this, refWatcher))
                .build();
    }

    public ApplicationComponent component() {
        if (component == null) {
            synchronized (this) {
                if (component == null) {
                    component = createComponent();
                }
            }
        }

        return component;
    }

    public RefWatcher refWatcher() {
        return refWatcher;
    }

}
