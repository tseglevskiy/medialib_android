package ru.roscha_akademii.medialib.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AndroidModule {
    private final MediaLibApplication application;

    public AndroidModule(MediaLibApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Context providesContext() {
        return application;
    }
    
    @Provides
    @Singleton
    public Resources providesResources() {
        return application.getResources();
    }

    @Provides
    @Singleton
    public SharedPreferences providesSharedPreferences(Context context) {
        return context.getSharedPreferences("app", Context.MODE_PRIVATE);
    }

}
