package ru.roscha_akademii.medialib.video;

import ru.roscha_akademii.medialib.common.CreateTableQueryBuilder;

import static ru.roscha_akademii.medialib.common.CreateTableQueryBuilder.SqlType.STRING;

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
                .column(VideoTable.TITLE, STRING)
                .column(VideoTable.DESCRIPTION, STRING)
                .column(VideoTable.PICTURE_URL, STRING)
                .column(VideoTable.VIDEO_URL, STRING)
                .build();
    }
}
