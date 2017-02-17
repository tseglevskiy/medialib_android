package ru.roscha_akademii.medialib.book.model.remote


import retrofit2.Call
import retrofit2.http.GET

interface BookApi {
    @GET("app/src/androidTest/assets/book.json")
    fun bookList(): Call<BookAnswer>

}
