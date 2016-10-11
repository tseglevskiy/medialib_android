package ru.roscha_akademii.medialib.video;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.roscha_akademii.medialib.common.CreateTableQueryBuilder;

import static ru.roscha_akademii.medialib.common.CreateTableQueryBuilder.SqlType.STRING;

public class VideoDb extends SQLiteOpenHelper {
    private static final int VERSION = 1;

    VideoDb(Context context, String tableName) {
        super(context, tableName, null, VERSION);
    }

    public static class VideoT {
        public static final String TABLE_NAME = "video";

        public static final String ID = "_id";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String PICTURE_URL = "picture_url";
        public static final String VIDEO_URL = "video_url";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(new CreateTableQueryBuilder(VideoT.TABLE_NAME)
                .index(VideoT.ID)
                .column(VideoT.TITLE, STRING)
                .column(VideoT.DESCRIPTION, STRING)
                .column(VideoT.PICTURE_URL, STRING)
                .column(VideoT.VIDEO_URL, STRING)
                .build());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO dummy upgrade
        db.execSQL("drop table if exists " + VideoT.TABLE_NAME);
        onCreate(db);
    }
}
