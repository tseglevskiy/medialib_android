package ru.roscha_akademii.medialib.book.model.remote

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.roscha_akademii.medialib.update.UpdateCallback

open class BookUpdate(val bookApi: BookApi) {
    open fun update(callback: UpdateCallback) {
        val call = bookApi.bookList()
        call.enqueue(object : Callback<BookAnswer> {
            override fun onResponse(call: Call<BookAnswer>, response: Response<BookAnswer>) {
                callback.onSuccess()
            }

            override fun onFailure(call: Call<BookAnswer>, t: Throwable) {
                callback.onFail()
            }

        })
    }
}