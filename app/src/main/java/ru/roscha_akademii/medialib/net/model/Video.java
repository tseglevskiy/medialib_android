package ru.roscha_akademii.medialib.net.model;

import com.google.gson.annotations.SerializedName;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

import ru.roscha_akademii.medialib.video.VideoDbSqliteHelper;

@StorIOSQLiteType(table = VideoDbSqliteHelper.VideoT.TABLE_NAME)
public class Video {
    @StorIOSQLiteColumn(name = VideoDbSqliteHelper.VideoT.ID, key = true)
    public long id;

    @StorIOSQLiteColumn(name = VideoDbSqliteHelper.VideoT.TITLE)
    public String title;

    @StorIOSQLiteColumn(name = VideoDbSqliteHelper.VideoT.PICTURE_URL)
    @SerializedName("picture")
    public String pictureUrl;

    @StorIOSQLiteColumn(name = VideoDbSqliteHelper.VideoT.DESCRIPTION)
    @SerializedName("desc")
    public String description;

    @SerializedName("length")
    public String duration;

    @SerializedName("dt")
    public String issueDate;

    @StorIOSQLiteColumn(name = VideoDbSqliteHelper.VideoT.VIDEO_URL)
    @SerializedName("video")
    public String videoUrl;
}
