package ru.roscha_akademii.medialib.video.model.remote;

import com.google.gson.annotations.SerializedName;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

import ru.roscha_akademii.medialib.video.model.local.VideoTable;

/*
Do not convert to Kotlin because
https://github.com/pushtorefresh/storio/issues/614
 */

@StorIOSQLiteType(table = VideoTable.TABLE_NAME)
public class Video {
    @StorIOSQLiteColumn(name = VideoTable.ID, key = true)
    public long id;

    @StorIOSQLiteColumn(name = VideoTable.TITLE)
    public String title;

    @StorIOSQLiteColumn(name = VideoTable.PICTURE_URL)
    @SerializedName("picture")
    public String pictureUrl;

    @StorIOSQLiteColumn(name = VideoTable.DESCRIPTION)
    @SerializedName("desc")
    public String description;

    @SerializedName("length")
    public String duration;

    @SerializedName("dt")
    public String issueDate;

    @StorIOSQLiteColumn(name = VideoTable.VIDEO_URL)
    @SerializedName("video")
    public String videoUrl;
}
