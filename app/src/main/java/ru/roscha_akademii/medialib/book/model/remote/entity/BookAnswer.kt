package ru.roscha_akademii.medialib.book.model.remote.entity

import com.google.gson.annotations.SerializedName
import java.util.*

data class BookAnswer(
        @SerializedName("list")
        var list: ArrayList<Book>? = null
)
