package ru.roscha_akademii.medialib.book.model.local

import com.pushtorefresh.storio.sqlite.StorIOSQLite
import com.pushtorefresh.storio.sqlite.queries.Query
import ru.roscha_akademii.medialib.book.model.local.entity.Book
import ru.roscha_akademii.medialib.book.model.local.entity.BookFile

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

    open fun saveBook(book: Book) {
        db
                .put()
                .`object`(book)
                .prepare()
                .executeAsBlocking()
    }

    open fun saveBookFile(file: BookFile) {
        db
                .put()
                .`object`(file)
                .prepare()
                .executeAsBlocking()
    }

    open fun saveBooks(list: List<Book>) {
        db
                .put()
                .objects(list)
                .prepare()
                .executeAsBlocking()
    }

    open fun getBook(id: Long): Book {
        try {
            return db
                    .get()
                    .listOfObjects(Book::class.java)
                    .withQuery(Query.builder()
                            .table(BookTable.TABLE_NAME)
                            .where(BookTable.ID + " = ?")
                            .whereArgs(id)
                            .build())
                    .prepare()
                    .executeAsBlocking()[0]
        } catch (e: IndexOutOfBoundsException) {
            throw UnexistingBookException(e)
        }
    }

    open fun getBookFile(id: Long): List<BookFile> {
        try {
            return db
                    .get()
                    .listOfObjects(BookFile::class.java)
                    .withQuery(Query.builder()
                            .table(BookFileTable.TABLE_NAME)
                            .where(BookFileTable.BOOK_ID + " = ?")
                            .whereArgs(id)
                            .build())
                    .prepare()
                    .executeAsBlocking()
        } catch (e: IndexOutOfBoundsException) {
            throw UnexistingBookException(e)
        }
    }
}


