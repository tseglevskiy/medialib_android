package ru.roscha_akademii.medialib.video

import android.content.Context

import com.pushtorefresh.storio.sqlite.StorIOSQLite
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite

import javax.inject.Named
import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import ru.roscha_akademii.medialib.net.model.Video
import ru.roscha_akademii.medialib.net.model.VideoSQLiteTypeMapping

@Module
open class VideoDbModule {

    @Provides
    @Singleton
    internal fun providesVideoStorage(@Named("video db") storio: StorIOSQLite): VideoStorage {
        return VideoStorage(storio)
    }



    @Provides
    @Singleton
    internal fun providesVideoDb(@Named("video db") storio: StorIOSQLite): VideoDb {
        return VideoDb(storio)
    }

    @Provides
    @Singleton
    @Named("video db")
    internal fun providesVideoDbSio(videoDbHelper: VideoDbSqliteHelper): StorIOSQLite {
        return DefaultStorIOSQLite
                .builder()
                .sqliteOpenHelper(videoDbHelper)
                .addTypeMapping(Video::class.java, VideoSQLiteTypeMapping()) // required for object mapping
                .build()
    }

    @Provides
    @Singleton
    internal fun providesVideoDbHelper(
            context: Context,
            @Named("video db filename") fileName: String?): VideoDbSqliteHelper {

        // null filename means in-memory
        // Dagger doesn't allow null values, so pass "" (empty string) instead null
        return VideoDbSqliteHelper(
                context,
                if (fileName?.isEmpty() ?: false) null else fileName) // transform "" => null
    }

    @Provides
    @Singleton
    @Named("video db filename")
    open fun providesVideoDbFileName(): String {
        return "video"
    }

}
