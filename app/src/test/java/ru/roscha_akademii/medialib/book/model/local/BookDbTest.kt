package ru.roscha_akademii.medialib.book.model.local

import com.pushtorefresh.storio.sqlite.StorIOSQLite
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import ru.roscha_akademii.medialib.BuildConfig
import ru.roscha_akademii.medialib.book.BookModule
import ru.roscha_akademii.medialib.book.model.local.entity.Book
import ru.roscha_akademii.medialib.book.model.local.entity.BookFile
import ru.roscha_akademii.medialib.common.AndroidModule
import ru.roscha_akademii.medialib.common.DaggerApplicationComponent
import ru.roscha_akademii.medialib.common.RobolectricMdiaLibApplication
import java.util.*

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class,
        sdk = intArrayOf(21),
        manifest = "AndroidManifest.xml",
        application = RobolectricMdiaLibApplication::class,
        packageName = "ru.roscha_akademii.medialib")

class BookDbTest {
    lateinit var bookDbStorIoHelper: StorIOSQLite
    lateinit var bookDbFileName: String

    lateinit var bookDb: BookDb // SUT

    /*
    boilerplate
     */

    @Before
    @Throws(Exception::class)
    fun setUp() {
        val app = RuntimeEnvironment.application as RobolectricMdiaLibApplication

        val component = DaggerApplicationComponent
                .builder()
                .androidModule(AndroidModule(app, app.refWatcher))
                .bookModule(object : BookModule() {
                    override fun providesBookDbFileName(): String {
                        return "" // in-memory database for tests
                    }
                })
                .build()

        app.setTestComponent(component)

        bookDbStorIoHelper = component.bookDbStorIo()

        bookDbFileName = component.bookDbFileName()

        bookDb = component.bookDb() // SUT
    }

    /*
    tests
     */

    @Test
    fun environment_inMemoryDb() {
        assertTrue("db filename for test has to be empty string", bookDbFileName.isEmpty())
    }

    @Test
    fun emptyDb_readAllBooks() {
        val list = bookDb.allBooks

        assertNotNull(list)
        assertEquals(0, list.size.toLong())
    }

    @Test
    fun twoBooks_readAllBooks() {
        bookDbStorIoHelper
                .put()
                .`object`(book1)
                .prepare()
                .executeAsBlocking()

        bookDbStorIoHelper
                .put()
                .`object`(book2)
                .prepare()
                .executeAsBlocking()

        val list = bookDb.allBooks

        assertNotNull(list)
        assertEquals(2, list.size.toLong())

        assertEquals(book1.id, list[0].id)
        assertEquals(book2.id, list[1].id)
    }

    @Test
    fun saveZeroBooks() {
        bookDb.saveBooks(ArrayList<Book>())

        val list = bookDb.allBooks
        assertNotNull(list)
        assertEquals(0, list.size.toLong())
    }

    @Test
    fun saveTwoBooks() {

        val listToSave = ArrayList<Book>()
        listToSave.add(book1)
        listToSave.add(book2)

        bookDb.saveBooks(listToSave)

        val list = bookDb.allBooks
        assertNotNull(list)
        assertEquals(2, list.size.toLong())

        assertEquals(book1.id, list[0].id)
        assertEquals(book2.id, list[1].id)
    }

    @Test
    fun saveTwoBooksOneByOne() {
        bookDb.saveBook(book1)
        bookDb.saveBook(book2)

        val list = bookDb.allBooks
        assertNotNull(list)
        assertEquals(2, list.size.toLong())

        assertEquals(book1.id, list[0].id)
        assertEquals(book2.id, list[1].id)
    }

    @Test
    fun saveOneBookTwoTimes() {
        bookDb.saveBook(book1) // save the book
        bookDb.saveBook(book1) // the same

        val list = bookDb.allBooks
        assertNotNull(list)
        assertEquals(1, list.size.toLong())

        assertEquals(book1.id, list[0].id)
    }

    @Test
    fun twoBooks_getBook_unexistingBook() {
        bookDbStorIoHelper
                .put()
                .`object`(book1)
                .prepare()
                .executeAsBlocking()

        bookDbStorIoHelper
                .put()
                .`object`(book2)
                .prepare()
                .executeAsBlocking()

        val unexistingId: Long = 3333
        assertNotEquals(unexistingId, book1.id)
        assertNotEquals(unexistingId, book2.id)

        try {
            bookDb.getBook(unexistingId)
            fail("taking unexisting video has to throw UnexistingVideoException")
        } catch (e: UnexistingBookException) {
            // OK
        }

    }

    @Test
    fun twoBooks_getBook_oneBook() {
        bookDbStorIoHelper
                .put()
                .`object`(book1)
                .prepare()
                .executeAsBlocking()

        bookDbStorIoHelper
                .put()
                .`object`(book2)
                .prepare()
                .executeAsBlocking()

        var book = bookDb.getBook(book1.id)

        assertEquals(book1.id, book.id)
        assertEquals(book1.description, book.description)

        book = bookDb.getBook(book2.id)

        assertEquals(book2.id, book.id)
        assertEquals(book2.description, book.description)

    }

    @Test
    fun bookFile_unexistingBookFile() {
        bookDb.saveBookFile(file11)
        bookDb.saveBookFile(file12)

        val list = bookDb.getBookFile(book2.id)
        assertNotNull(list)
        assertEquals(0, list.size)
    }

    @Test
    fun bookFile_oneBookFile() {
        // file for 'book2'
        assertEquals(file21.bookId, book2.id)
        bookDb.saveBookFile(file21)

        // other files
        assertNotEquals(file11.bookId, book2.id)
        bookDb.saveBookFile(file11)
        assertNotEquals(file12.bookId, book2.id)
        bookDb.saveBookFile(file12)

        val list = bookDb.getBookFile(book2.id)
        assertNotNull(list)
        assertEquals(1, list.size)
        assertEquals(book2.id, list[0].bookId)
        assertEquals(file21.url, list[0].url)
    }

    @Test
    fun bookFile_twoBookFiles() {
        // files for 'bookDto1'
        assertEquals(file11.bookId, book1.id)
        bookDb.saveBookFile(file11)
        assertEquals(file12.bookId, book1.id)
        bookDb.saveBookFile(file12)

        // other files
        assertNotEquals(file21.bookId, book1.id)
        bookDb.saveBookFile(file21)

        val list = bookDb
                .getBookFile(book1.id)
                .sortedBy(BookFile::url)

        assertNotNull(list)
        assertEquals(2, list.size)
        assertEquals(book1.id, list[0].bookId)
        assertEquals(book1.id, list[1].bookId)
        assertEquals(file11.url, list[0].url)
        assertEquals(file12.url, list[1].url)
    }

    companion object {

        /*
        test data
         */
        private val book1 = Book(
                1111,
                "title one",
                "picture url one",
                "description one")

        private val book2 = Book(
                2222,
                "title two",
                "picture url two",
                "description two")

        private val file11 = BookFile(
                book1.id,
                "http://file.1.1"
        )

        private val file12 = BookFile(
                book1.id,
                "http://file.1.2"
        )

        private val file21 = BookFile(
                book2.id,
                "http://file.2.1"
        )    }
}