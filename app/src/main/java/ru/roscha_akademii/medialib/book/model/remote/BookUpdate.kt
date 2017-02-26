package ru.roscha_akademii.medialib.book.model.remote

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.roscha_akademii.medialib.book.model.local.BookDb
import ru.roscha_akademii.medialib.book.model.local.entity.Book
import ru.roscha_akademii.medialib.book.model.local.entity.BookFile
import ru.roscha_akademii.medialib.book.model.remote.entity.BookAnswer
import ru.roscha_akademii.medialib.book.model.remote.entity.BookDTO
import ru.roscha_akademii.medialib.storage.Storage
import ru.roscha_akademii.medialib.update.UpdateCallback

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

    private fun saveBooks(list: List<BookDTO>) {
        list.forEach { dto ->
            run {
                val book = Book(dto)
                bookDb.saveBook(book)

                dto.files
                        ?.distinct()
                        ?.forEach { url ->
                            run {
                                bookDb.saveBookFile(BookFile(book, url))
                            }
                        }
            }
        }

        list
                .map { it.picture }
                .filterNotNull()
                .forEach { storage.saveLocal(it, "image", false) }

    }
}