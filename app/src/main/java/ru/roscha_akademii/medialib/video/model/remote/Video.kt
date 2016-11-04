package ru.roscha_akademii.medialib.video.model.remote

import com.google.gson.annotations.SerializedName

data class Video(
        val id: Long,

        val title: String? = null,

        @SerializedName("picture")
        val pictureUrl: String? = null,

        @SerializedName("desc")
        val description: String? = null,

        //    @SerializedName("length")
//    var duration: String? = null

//    @SerializedName("dt")
//    var issueDate: String? = null

        @SerializedName("video")
        val videoUrl: String
)


