package ru.roscha_akademii.medialib.net.model;

import com.google.gson.annotations.SerializedName;

public class Video {
    public String title;
    @SerializedName("picture")
    public String pictureUrl;
    @SerializedName("desc")
    public String description;
    @SerializedName("length")
    public String duration;
    @SerializedName("dt")
    public String issueDate;
    @SerializedName("video")
    public String videoUrl;
}
