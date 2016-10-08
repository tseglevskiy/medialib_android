package ru.roscha_akademii.medialib.common;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import javax.inject.Singleton;

import dagger.Component;
import io.fabric.sdk.android.Fabric;
import ru.roscha_akademii.medialib.main.presenter.MainPresenterImpl;
import ru.roscha_akademii.medialib.main.view.MainActivity;
import ru.roscha_akademii.medialib.update.UpdateService;

public class MediaLibApplication extends Application {
    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        refWatcher = LeakCanary.install(this);
        Fabric.with(this, new Crashlytics());
    }

    protected ApplicationComponent component;

    @Singleton
    @Component(modules = {
            AndroidModule.class
    })
    public interface ApplicationComponent {
        void inject(MainActivity activity);

        void inject(MainPresenterImpl mainPresenter);

        void inject(UpdateService updateService);
    }

    private ApplicationComponent createComponent() {
        return DaggerMediaLibApplication_ApplicationComponent
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
}
