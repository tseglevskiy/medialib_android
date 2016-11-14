package ru.roscha_akademii.medialib.storage;

import ru.roscha_akademii.medialib.common.CreateTableQueryBuilder;

public class StorageTable {
    public static final int VERSION = 3;

    public static final String TABLE_NAME = "video_storage";

    public static final String REMOTE_URI = "remote_uri";
    public static final String STATUS = "status";
    public static final String LOCAL_URI = "local_uri";
    public static final String DOWNLOAD_ID = "download_id";
    public static final String PERCENT = "percent";

    public static String createTable() {
        return new CreateTableQueryBuilder(StorageTable.TABLE_NAME)
                .index(StorageTable.REMOTE_URI, CreateTableQueryBuilder.SqlType.STRING)
                .column(StorageTable.STATUS, CreateTableQueryBuilder.SqlType.INTEGER)
                .column(StorageTable.LOCAL_URI, CreateTableQueryBuilder.SqlType.STRING)
                .column(StorageTable.DOWNLOAD_ID, CreateTableQueryBuilder.SqlType.INTEGER)
                .column(StorageTable.PERCENT, CreateTableQueryBuilder.SqlType.INTEGER)
                .build();
    }
}
