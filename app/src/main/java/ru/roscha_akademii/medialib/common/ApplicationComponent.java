package ru.roscha_akademii.medialib.common;

import android.content.Context;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;
import ru.roscha_akademii.medialib.net.NetModule;
import ru.roscha_akademii.medialib.net.VideoApi;
import ru.roscha_akademii.medialib.update.UpdateModule;
import ru.roscha_akademii.medialib.update.UpdateScheduler;
import ru.roscha_akademii.medialib.update.UpdateService;
import ru.roscha_akademii.medialib.video.VideoDb;
import ru.roscha_akademii.medialib.video.VideoDbSqliteHelper;
import ru.roscha_akademii.medialib.video.VideoDbModule;

import static ru.roscha_akademii.medialib.net.NetModule.BASE_URL;
import static ru.roscha_akademii.medialib.video.VideoDbModule.VIDEO_DB;
import static ru.roscha_akademii.medialib.video.VideoDbModule.VIDEO_DB_FILENAME;

@Singleton
@Component(modules = {
        AndroidModule.class,
        NetModule.class,
        VideoDbModule.class,
        UpdateModule.class
})
public interface ApplicationComponent {
    /*
    ActivityScope components
     */

    ActivityComponent activityComponent(ActivityModule am);

    /*
    Injectors
     */

    void inject(UpdateService updateService);

    /*
    Using submodules requires providing explicit methods in the parent component.
     */

    // AndroidModule

    Context context();

    // NetModule

    @Named(BASE_URL)
    String serverBaseUrl();

    VideoApi videoApi();

    // VideoDbModule

    @Named(VIDEO_DB_FILENAME)
    String videoDbFileName();

    VideoDbSqliteHelper videoDbSqliteHelper();

    @Named(VIDEO_DB)
    StorIOSQLite videoDbStorIo();

    VideoDb videoDb();

    // UpdateModule

    UpdateScheduler updateScheduler();

}
