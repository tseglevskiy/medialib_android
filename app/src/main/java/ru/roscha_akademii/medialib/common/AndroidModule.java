package ru.roscha_akademii.medialib.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.squareup.leakcanary.RefWatcher;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AndroidModule {
    private final MediaLibApplication application;
    private final RefWatcher refWatcher;

    public AndroidModule(MediaLibApplication application, RefWatcher refWatcher) {
        this.application = application;
        this.refWatcher = refWatcher;
    }

    @Provides
    @Singleton
    Context providesContext() {
        return application;
    }

    @Provides
    @Singleton
    RefWatcher providesRefWatcher() {
        return refWatcher;
    }
    
    @Provides
    @Singleton
    Resources providesResources() {
        return application.getResources();
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Context context) {
        return context.getSharedPreferences("app", Context.MODE_PRIVATE);
    }

}
