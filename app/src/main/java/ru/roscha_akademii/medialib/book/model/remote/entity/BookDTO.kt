package ru.roscha_akademii.medialib.book.model.remote.entity

import com.google.gson.annotations.SerializedName

data class BookDTO(
        val id: Long,

        @SerializedName("title")
        val title: String? = null,

        @SerializedName("picture")
        var picture: String? = null,

        @SerializedName("desc")
        var description: String? = null,

        @SerializedName("files")
        var files: List<String>? = null
)
