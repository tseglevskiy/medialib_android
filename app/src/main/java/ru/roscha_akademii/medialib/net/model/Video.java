package ru.roscha_akademii.medialib.net.model;

import com.google.gson.annotations.SerializedName;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

import ru.roscha_akademii.medialib.video.VideoDb;

@StorIOSQLiteType(table = VideoDb.VideoT.TABLE_NAME)
public class Video {
    @StorIOSQLiteColumn(name = VideoDb.VideoT.ID, key = true)
    public long id;

    @StorIOSQLiteColumn(name = VideoDb.VideoT.TITLE)
    public String title;

    @StorIOSQLiteColumn(name = VideoDb.VideoT.PICTURE_URL)
    @SerializedName("picture")
    public String pictureUrl;

    @StorIOSQLiteColumn(name = VideoDb.VideoT.DESCRIPTION)
    @SerializedName("desc")
    public String description;

    @SerializedName("length")
    public String duration;

    @SerializedName("dt")
    public String issueDate;

    @StorIOSQLiteColumn(name = VideoDb.VideoT.VIDEO_URL)
    @SerializedName("video")
    public String videoUrl;
}
