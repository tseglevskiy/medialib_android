package ru.roscha_akademii.medialib.video;

import ru.roscha_akademii.medialib.common.CreateTableQueryBuilder;

import static ru.roscha_akademii.medialib.common.CreateTableQueryBuilder.SqlType.INTEGER;
import static ru.roscha_akademii.medialib.common.CreateTableQueryBuilder.SqlType.STRING;

public class VideoStorageTable {
    public static final String TABLE_NAME = "video_storage";

    public static final String ID = "_id";
    public static final String STATUS = "status";
    public static final String LOCAL_URI = "local_uri";
    public static final String DOWNLOAD_ID = "download_id";

    public static String createTable() {
        return new CreateTableQueryBuilder(VideoStorageTable.TABLE_NAME)
                .index(VideoStorageTable.ID)
                .column(VideoStorageTable.STATUS, INTEGER)
                .column(VideoStorageTable.LOCAL_URI, STRING)
                .column(VideoStorageTable.DOWNLOAD_ID, INTEGER)
                .build();
    }
}
