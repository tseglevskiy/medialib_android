package ru.roscha_akademii.medialib.video.model.local;

import ru.roscha_akademii.medialib.common.CreateTableQueryBuilder;

public class VideoStorageTable {
    public static final int VERSION = 3;

    public static final String TABLE_NAME = "video_storage";

    public static final String ID = "_id";
    public static final String STATUS = "status";
    public static final String LOCAL_URI = "local_uri";
    public static final String DOWNLOAD_ID = "download_id";
    public static final String PERCENT = "percent";

    public static String createTable() {
        return new CreateTableQueryBuilder(VideoStorageTable.TABLE_NAME)
                .index(VideoStorageTable.ID)
                .column(VideoStorageTable.STATUS, CreateTableQueryBuilder.SqlType.INTEGER)
                .column(VideoStorageTable.LOCAL_URI, CreateTableQueryBuilder.SqlType.STRING)
                .column(VideoStorageTable.DOWNLOAD_ID, CreateTableQueryBuilder.SqlType.INTEGER)
                .column(VideoStorageTable.PERCENT, CreateTableQueryBuilder.SqlType.INTEGER)
                .build();
    }
}
