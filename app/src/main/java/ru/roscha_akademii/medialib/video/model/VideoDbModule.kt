package ru.roscha_akademii.medialib.video.model

import android.app.DownloadManager
import android.content.ContentResolver
import android.content.Context

import com.pushtorefresh.storio.sqlite.StorIOSQLite
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite

import javax.inject.Named
import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.roscha_akademii.medialib.video.model.local.*
import ru.roscha_akademii.medialib.video.model.local.storio.VideoMapping
import ru.roscha_akademii.medialib.video.model.local.storio.VideoStorageRecordMapping
import ru.roscha_akademii.medialib.video.model.remote.Video
import ru.roscha_akademii.medialib.video.model.remote.VideoApi

@Module
open class VideoDbModule {

    @Provides
    @Singleton
    internal fun providesVideoStorage(@Named("video db") storio: StorIOSQLite,
                                      videoDb: VideoDb,
                                      context: Context,
                                      contentResolver: ContentResolver,
                                      downloadManager: DownloadManager)
            : VideoStorage {
        return VideoStorage(storio, videoDb, context, contentResolver, downloadManager)
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
                .addTypeMapping(Video::class.java, VideoMapping())
                .addTypeMapping(VideoStorageRecord::class.java, VideoStorageRecordMapping())
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

    @Provides
    @Singleton
    internal fun providesVideoApi(retrofit: Retrofit): VideoApi {
        return retrofit.create(VideoApi::class.java)
    }
}
