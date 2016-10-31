package ru.roscha_akademii.medialib.video;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.roscha_akademii.medialib.common.CreateTableQueryBuilder;

import static ru.roscha_akademii.medialib.common.CreateTableQueryBuilder.SqlType.STRING;

public class VideoDbSqliteHelper extends SQLiteOpenHelper {
    private static final int VERSION = 2;

    VideoDbSqliteHelper(Context context, String tableName) {
        super(context, tableName, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(VideoTable.createTable());
        db.execSQL(VideoStorageTable.createTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO dummy upgrade
        db.execSQL("drop table if exists " + VideoTable.TABLE_NAME);
        db.execSQL("drop table if exists " + VideoStorageTable.TABLE_NAME);
        onCreate(db);
    }
}
