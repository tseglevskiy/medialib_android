package ru.roscha_akademii.medialib.common;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;
import io.fabric.sdk.android.Fabric;
import ru.roscha_akademii.medialib.main.presenter.MainPresenterImpl;
import ru.roscha_akademii.medialib.main.view.MainActivity;
import ru.roscha_akademii.medialib.net.BaseUrlModule;
import ru.roscha_akademii.medialib.net.NetModule;
import ru.roscha_akademii.medialib.update.UpdateService;
import ru.roscha_akademii.medialib.video.VideoDb;
import ru.roscha_akademii.medialib.video.VideoDbModule;

import static ru.roscha_akademii.medialib.video.VideoDbModule.VIDEO_DB;
import static ru.roscha_akademii.medialib.video.VideoDbModule.VIDEO_DB_FILENAME;

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
            AndroidModule.class,
            BaseUrlModule.class,
            NetModule.class,
            VideoDbModule.class
    })
    public interface ApplicationComponent {
        void inject(MainActivity activity);

        void inject(MainPresenterImpl mainPresenter);

        void inject(UpdateService updateService);

        // VideoDbModule

        @Named(VIDEO_DB_FILENAME)
        String videoDbFileName();

        VideoDb videoDbSqliteHelper();

        @Named(VIDEO_DB)
        StorIOSQLite videoDbStorIo();
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
