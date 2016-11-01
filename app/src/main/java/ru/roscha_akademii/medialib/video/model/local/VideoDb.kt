package ru.roscha_akademii.medialib.video.model.local

import com.pushtorefresh.storio.sqlite.StorIOSQLite
import com.pushtorefresh.storio.sqlite.queries.Query
import ru.roscha_akademii.medialib.video.model.UnexistingVideoException
import ru.roscha_akademii.medialib.video.model.remote.Video

open class VideoDb(internal var db: StorIOSQLite) {

    open val allVideo: List<Video>
        get() = db
                .get()
                .listOfObjects(Video::class.java)
                .withQuery(Query.builder()
                        .table(VideoTable.TABLE_NAME)
                        .orderBy(VideoTable.ID)
                        .build())
                .prepare()
                .executeAsBlocking()

    open fun getVideo(id: Long): Video {
        try {
            return db
                    .get()
                    .listOfObjects(Video::class.java)
                    .withQuery(Query.builder()
                            .table(VideoTable.TABLE_NAME)
                            .where(VideoTable.ID + " = ?")
                            .whereArgs(id)
                            .orderBy(VideoTable.ID)
                            .build())
                    .prepare()
                    .executeAsBlocking()[0]
        } catch (e: IndexOutOfBoundsException) {
            throw UnexistingVideoException(e)
        }
    }


}
