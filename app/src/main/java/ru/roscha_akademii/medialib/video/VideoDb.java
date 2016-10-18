package ru.roscha_akademii.medialib.video;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.List;

import ru.roscha_akademii.medialib.net.model.Video;

public class VideoDb {
    StorIOSQLite db;

    public VideoDb(StorIOSQLite db) {
        this.db = db;
    }

    public List<Video> getAllVideo() {
        return db
                .get()
                .listOfObjects(Video.class)
                .withQuery(Query.builder()
                        .table(VideoDbSqliteHelper.VideoT.TABLE_NAME)
                        .orderBy(VideoDbSqliteHelper.VideoT.ID)
                        .build())
                .prepare()
                .executeAsBlocking();
    }

    public Video getVideo(long id) {
        try {
            return db
                    .get()
                    .listOfObjects(Video.class)
                    .withQuery(Query.builder()
                            .table(VideoDbSqliteHelper.VideoT.TABLE_NAME)
                            .where(VideoDbSqliteHelper.VideoT.ID + " = ?")
                            .whereArgs(id)
                            .orderBy(VideoDbSqliteHelper.VideoT.ID)
                            .build())
                    .prepare()
                    .executeAsBlocking()
                    .get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
}
