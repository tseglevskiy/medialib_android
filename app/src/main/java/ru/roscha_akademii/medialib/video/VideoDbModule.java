package ru.roscha_akademii.medialib.video;

import android.content.Context;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.roscha_akademii.medialib.net.model.Video;
import ru.roscha_akademii.medialib.net.model.VideoSQLiteTypeMapping;

@Module
public class VideoDbModule {
    public static final String VIDEO_DB = "videodb";

    @Provides
    @Singleton
    @NonNull
    @Named(VIDEO_DB)
    StorIOSQLite providesVideoDbSio(VideoDb videoDbHelper) {
        return DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(videoDbHelper)
                .addTypeMapping(Video.class, new VideoSQLiteTypeMapping()) // required for object mapping
                .build();
    }

    @Provides
    @Singleton
    @NonNull
    VideoDb providesVideoDbHelper(
            Context context,
            @Named(VIDEO_DB_FILENAME) String fileName)
    {
        // null filename means in-memory
        // Dagger doesn't allow null values, so pass "" (empty string) instead null
        if (fileName.isEmpty()) {
            fileName = null;
        }
        return new VideoDb(context, fileName);
    }

    public static final String VIDEO_DB_FILENAME = "video db filename";

    @Provides
    @Singleton
    @Named(VIDEO_DB_FILENAME)
    public String providesVideoDbFileName() {
        return "video";
    }
}
