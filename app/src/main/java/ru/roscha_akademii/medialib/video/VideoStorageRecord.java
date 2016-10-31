package ru.roscha_akademii.medialib.video;

import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

/*
Do not convert to Kotlin because
https://github.com/pushtorefresh/storio/issues/614
 */

@StorIOSQLiteType(table = VideoStorageTable.TABLE_NAME)
public class VideoStorageRecord {
    @StorIOSQLiteColumn(name = VideoStorageTable.ID, key = true)
    public long id;

    @StorIOSQLiteColumn(name = VideoStorageTable.LOCAL_URI)
    public String localUri;

    @StorIOSQLiteColumn(name = VideoStorageTable.STATUS)
    public String status;

    @StorIOSQLiteColumn(name = VideoStorageTable.DOWNLOAD_ID)
    public String downloadId;

}