package ru.roscha_akademii.medialib.book.model.local

import com.pushtorefresh.storio.sqlite.StorIOSQLite
import com.pushtorefresh.storio.sqlite.queries.Query
import ru.roscha_akademii.medialib.book.model.remote.entity.Book
import java.util.*

open class BookDb (internal var db: StorIOSQLite) {

    open val allBooks: List<Book>
        get() = db
                .get()
                .listOfObjects(Book::class.java)
                .withQuery(Query.builder()
                        .table(BookTable.TABLE_NAME)
                        .orderBy(BookTable.ID)
                        .build())
                .prepare()
                .executeAsBlocking()

    open fun saveBooks(list: ArrayList<Book>) {
        db
                .put()
                .objects(list)
                .prepare()
                .executeAsBlocking()
    }
}


