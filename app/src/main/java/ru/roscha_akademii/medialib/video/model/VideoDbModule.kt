package ru.roscha_akademii.medialib.video.model

import android.content.Context
import com.pushtorefresh.storio.sqlite.StorIOSQLite
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.roscha_akademii.medialib.storage.Storage
import ru.roscha_akademii.medialib.video.model.local.VideoDb
import ru.roscha_akademii.medialib.video.model.local.VideoDbSqliteHelper
import ru.roscha_akademii.medialib.video.model.local.storio.VideoMapping
import ru.roscha_akademii.medialib.video.model.remote.VideoApi
import ru.roscha_akademii.medialib.video.model.remote.VideoUpdate
import ru.roscha_akademii.medialib.video.model.remote.entity.Video
import javax.inject.Named
import javax.inject.Singleton

@Module
open class VideoDbModule {

    @Provides
    @Singleton
    internal fun providesVideoDb(@Named("video db") storio: StorIOSQLite)
            : VideoDb
            = VideoDb(storio)

    @Provides
    @Singleton
    @Named("video db")
    internal fun providesVideoDbSio(videoDbHelper: VideoDbSqliteHelper)
            : StorIOSQLite
            = DefaultStorIOSQLite
            .builder()
            .sqliteOpenHelper(videoDbHelper)
            .addTypeMapping(Video::class.java, VideoMapping())
            .build()

    @Provides
    @Singleton
    internal fun providesVideoDbHelper(context: Context,
                                       @Named("video db filename") fileName: String?)
            : VideoDbSqliteHelper
            // null filename means in-memory
            // Dagger doesn't allow null values, so pass "" (empty string) instead null
            = VideoDbSqliteHelper(context, if (fileName?.isEmpty() ?: false) null else fileName) // transform "" => null

    @Provides
    @Singleton
    @Named("video db filename")
    open fun providesVideoDbFileName()
            : String
            = "video"

    @Provides
    @Singleton
    internal fun providesVideoApi(retrofit: Retrofit)
            : VideoApi
            = retrofit.create(VideoApi::class.java)

    @Provides
    @Singleton
    open fun providesVideoUpdate(videoApi: VideoApi,
                            @Named("video db") videoDb: StorIOSQLite,
                            storage: Storage)
            : VideoUpdate
            = VideoUpdate(videoApi = videoApi, videoDb = videoDb, storage = storage)
}
