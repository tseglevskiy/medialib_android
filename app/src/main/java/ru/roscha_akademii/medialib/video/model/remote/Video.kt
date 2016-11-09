package ru.roscha_akademii.medialib.video.model.remote

import com.google.gson.annotations.SerializedName
import org.joda.time.LocalDate

data class Video(
        val id: Long,

        val title: String? = null,

        @SerializedName("picture")
        val pictureUrl: String? = null,

        @SerializedName("desc")
        val description: String? = null,

        @SerializedName("length")
        val duration: String? = null,

        @SerializedName("dt")
        val issueDate: LocalDate? = null,

        @SerializedName("video")
        val videoUrl: String
)


