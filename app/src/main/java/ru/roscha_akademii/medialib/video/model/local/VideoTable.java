package ru.roscha_akademii.medialib.video.model.local;

import ru.roscha_akademii.medialib.common.CreateTableQueryBuilder;

public class VideoTable {
    public static final String TABLE_NAME = "video";

    public static final String ID = "_id";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String PICTURE_URL = "picture_url";
    public static final String VIDEO_URL = "video_url";

    public static String createTable() {
        return new CreateTableQueryBuilder(VideoTable.TABLE_NAME)
                .index(VideoTable.ID)
                .column(VideoTable.TITLE, CreateTableQueryBuilder.SqlType.STRING)
                .column(VideoTable.DESCRIPTION, CreateTableQueryBuilder.SqlType.STRING)
                .column(VideoTable.PICTURE_URL, CreateTableQueryBuilder.SqlType.STRING)
                .column(VideoTable.VIDEO_URL, CreateTableQueryBuilder.SqlType.STRING)
                .build();
    }
}
