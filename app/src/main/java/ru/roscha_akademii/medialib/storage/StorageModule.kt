package ru.roscha_akademii.medialib.storage

import android.app.DownloadManager
import android.content.ContentResolver
import android.content.Context
import com.pushtorefresh.storio.sqlite.StorIOSQLite
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite
import dagger.Module
import dagger.Provides
import ru.roscha_akademii.medialib.video.model.local.StorageDbSqliteHelper
import ru.roscha_akademii.medialib.video.model.local.VideoDb
import ru.roscha_akademii.medialib.video.model.local.VideoDbSqliteHelper
import ru.roscha_akademii.medialib.video.model.local.storio.VideoMapping
import ru.roscha_akademii.medialib.video.model.remote.Video
import javax.inject.Named
import javax.inject.Singleton

@Module
open class StorageModule() {

    @Provides
    @Singleton
    internal fun providesVideoStorage(@Named("storage db") storio: StorIOSQLite,
                                      context: Context,
                                      contentResolver: ContentResolver,
                                      downloadManager: DownloadManager) : Storage
            = StorageImpl(
            db = storio,
            context = context,
            contentResolver = contentResolver,
            downloadManager = downloadManager)


    @Provides
    @Singleton
    @Named("storage db")
    internal fun providesStorageDbSio(videoDbHelper: VideoDbSqliteHelper)
            : StorIOSQLite
            = DefaultStorIOSQLite
            .builder()
            .sqliteOpenHelper(videoDbHelper)
            .addTypeMapping(Video::class.java, VideoMapping())
            .addTypeMapping(VideoStorageRecord::class.java, StorageRecordMapping())
            .build()

    @Provides
    @Singleton
    internal fun providesStorageDbHelper(context: Context,
                                       @Named("storage db filename") fileName: String?)
            : StorageDbSqliteHelper
            // null filename means in-memory
            // Dagger doesn't allow null values, so pass "" (empty string) instead null
            = StorageDbSqliteHelper(context, if (fileName?.isEmpty() ?: false) null else fileName) // transform "" => null

    @Provides
    @Singleton
    @Named("storage db filename")
    open fun providesStorageDbFileName()
            : String
            = "storage"

}