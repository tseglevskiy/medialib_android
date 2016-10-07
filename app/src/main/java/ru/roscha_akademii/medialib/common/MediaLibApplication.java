package ru.roscha_akademii.medialib.common;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import javax.inject.Singleton;

import dagger.Component;
import io.fabric.sdk.android.Fabric;
import ru.roscha_akademii.medialib.MainActivity;

public class MediaLibApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
    }

    protected ApplicationComponent component;

    @Singleton
    @Component(modules = {
            AndroidModule.class
    })
    public interface ApplicationComponent {
        void inject(MainActivity activity);
    }

    private ApplicationComponent createComponent() {
        return DaggerMediaLibApplication_ApplicationComponent
                .builder()
                .androidModule(new AndroidModule(this))
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
}
