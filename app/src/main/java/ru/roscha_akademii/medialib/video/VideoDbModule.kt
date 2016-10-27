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

const val VIDEO_DB = "videodb"
const val VIDEO_DB_FILENAME = "video db filename"

@Module
open class VideoDbModule {


    @Provides
    @Singleton
    internal fun providesVideoDb(@Named(VIDEO_DB) storio: StorIOSQLite): VideoDb {
        return VideoDb(storio)
    }

    @Provides
    @Singleton
    @Named(VIDEO_DB)
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
            @Named(VIDEO_DB_FILENAME) fileName: String?): VideoDbSqliteHelper {

        // null filename means in-memory
        // Dagger doesn't allow null values, so pass "" (empty string) instead null
        return VideoDbSqliteHelper(
                context,
                if (fileName?.isEmpty() ?: false) null else fileName) // transform "" => null
    }

    @Provides
    @Singleton
    @Named(VIDEO_DB_FILENAME)
    open fun providesVideoDbFileName(): String {
        return "video"
    }

}
