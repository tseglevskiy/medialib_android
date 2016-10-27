package ru.roscha_akademii.medialib.video

import com.pushtorefresh.storio.sqlite.StorIOSQLite
import com.pushtorefresh.storio.sqlite.queries.Query

import ru.roscha_akademii.medialib.net.model.Video

open class VideoDb(internal var db: StorIOSQLite) {

    open val allVideo: List<Video>
        get() = db
                .get()
                .listOfObjects(Video::class.java)
                .withQuery(Query.builder()
                        .table(VideoDbSqliteHelper.VideoT.TABLE_NAME)
                        .orderBy(VideoDbSqliteHelper.VideoT.ID)
                        .build())
                .prepare()
                .executeAsBlocking()

    open fun getVideo(id: Long): Video {
            return db
                    .get()
                    .listOfObjects(Video::class.java)
                    .withQuery(Query.builder()
                            .table(VideoDbSqliteHelper.VideoT.TABLE_NAME)
                            .where(VideoDbSqliteHelper.VideoT.ID + " = ?")
                            .whereArgs(id)
                            .orderBy(VideoDbSqliteHelper.VideoT.ID)
                            .build())
                    .prepare()
                    .executeAsBlocking()[0]

    }
}
