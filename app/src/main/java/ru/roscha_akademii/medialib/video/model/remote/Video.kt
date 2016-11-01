package ru.roscha_akademii.medialib.video.model.remote

import com.google.gson.annotations.SerializedName

class Video {
    constructor() {
    }

    constructor(id: Long, title: String?, pictureUrl: String?, description: String?, videoUrl: String?) {
        this.id = id
        this.title = title
        this.pictureUrl = pictureUrl
        this.description = description
        this.videoUrl = videoUrl
    }

    var id: Long = 0

    var title: String? = null

    @SerializedName("picture")
    var pictureUrl: String? = null

    @SerializedName("desc")
    var description: String? = null

//    @SerializedName("length")
//    var duration: String? = null

//    @SerializedName("dt")
//    var issueDate: String? = null

    @SerializedName("video")
    var videoUrl: String? = null

}
