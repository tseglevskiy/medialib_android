package ru.roscha_akademii.medialib.book.model.remote

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.roscha_akademii.medialib.book.model.local.BookDb
import ru.roscha_akademii.medialib.book.model.remote.entity.Book
import ru.roscha_akademii.medialib.book.model.remote.entity.BookAnswer
import ru.roscha_akademii.medialib.storage.Storage
import ru.roscha_akademii.medialib.update.UpdateCallback
import java.util.*

open class BookUpdate(val bookApi: BookApi,
                      val bookDb: BookDb,
                      val storage: Storage) {

    open fun update(callback: UpdateCallback) {
        val call = bookApi.bookList()
        call.enqueue(object : Callback<BookAnswer> {
            override fun onResponse(call: Call<BookAnswer>, response: Response<BookAnswer>) {
                try {
                    response.body()?.list?.let {
                        saveBooks(it)
                    }

                    callback.onSuccess()
                    return

                } catch (ignore: Exception) {
                    // ничего, в следующий раз получится
                }

                callback.onFail()
            }

            override fun onFailure(call: Call<BookAnswer>, t: Throwable) {
                callback.onFail()
            }

        })
    }

    private fun saveBooks(list: ArrayList<Book>) {
        bookDb.saveBooks(list)

        list
                .map { it.picture }
                .filterNotNull()
                .forEach { storage.saveLocal(it, "image", false) }

    }
}