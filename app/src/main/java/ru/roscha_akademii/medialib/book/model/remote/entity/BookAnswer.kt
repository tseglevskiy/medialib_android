package ru.roscha_akademii.medialib.book.model.remote.entity

import com.google.gson.annotations.SerializedName

data class BookAnswer(
        @SerializedName("list")
        var list: List<BookDTO>? = null
)
