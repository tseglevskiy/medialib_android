package ru.roscha_akademii.medialib;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by VOpolski on 14.09.2016.
 */
public class LeakCanaryApplication extends Application {
    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        refWatcher = LeakCanary.install(this);
    }

    public static RefWatcher getRefWatcher(Context context) {
        LeakCanaryApplication application =
                (LeakCanaryApplication) context.getApplicationContext();
        return application.refWatcher;
    }
}
