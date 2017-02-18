package ru.roscha_akademii.medialib.book.model.local

import com.pushtorefresh.storio.sqlite.StorIOSQLite
import com.pushtorefresh.storio.sqlite.queries.Query
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import ru.roscha_akademii.medialib.BuildConfig
import ru.roscha_akademii.medialib.book.BookModule
import ru.roscha_akademii.medialib.book.model.remote.entity.Book
import ru.roscha_akademii.medialib.common.AndroidModule
import ru.roscha_akademii.medialib.common.DaggerApplicationComponent
import ru.roscha_akademii.medialib.common.RobolectricMdiaLibApplication

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class,
        sdk = intArrayOf(21),
        manifest = "AndroidManifest.xml",
        application = RobolectricMdiaLibApplication::class,
        packageName = "ru.roscha_akademii.medialib")

class BookDbStorIoTest {
    lateinit var bookDb: StorIOSQLite // SUT

    lateinit var bookDbFilename: String
    lateinit var bookDbSqliteHelper: BookDbSqliteHelper

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

        bookDb = component.bookDbStorIo() // SUT

        bookDbFilename = component.bookDbFileName()
        bookDbSqliteHelper = component.bookDbSqliteHelper()
    }

    /*
    tests
     */

    @Test
    fun environment_inMemoryDb() {
        assertTrue("db filename for test has to be empty string", bookDbFilename.isEmpty())
    }

    @Test
    fun environment_inMemoryDbDeFacto() {
        assertNull("db has to be in-memory", bookDbSqliteHelper.databaseName)
    }

    @Test
    fun writeOneItem_readManually() {
        bookDb
                .put()
                .`object`(book1)
                .prepare()
                .executeAsBlocking()

        val cursor = bookDbSqliteHelper
                .readableDatabase
                .rawQuery("select * from " + BookTable.TABLE_NAME, null)

        assertEquals("added one object", 1, cursor.count.toLong())

        val desriptionIdx = cursor.getColumnIndex(BookTable.DESCRIPTION)
        val idIdx = cursor.getColumnIndex(BookTable.ID)
        val titleIdx = cursor.getColumnIndex(BookTable.TITLE)
        val pictureUrlIdx = cursor.getColumnIndex(BookTable.PICTURE_URL)

        assertTrue(cursor.moveToFirst())

        assertEquals(book1.id, cursor.getLong(idIdx))
        assertEquals(book1.description, cursor.getString(desriptionIdx))
        assertEquals(book1.title, cursor.getString(titleIdx))
        assertEquals(book1.picture, cursor.getString(pictureUrlIdx))

        cursor.close()
    }

    @Test
    fun writeTwoItems_readByStorIo() {
        bookDb
                .put()
                .`object`(book1)
                .prepare()
                .executeAsBlocking()

        bookDb
                .put()
                .`object`(book2)
                .prepare()
                .executeAsBlocking()

        val readedList = bookDb
                .get()
                .listOfObjects(Book::class.java)
                .withQuery(Query.builder()
                        .table(BookTable.TABLE_NAME)
                        .orderBy(BookTable.ID)
                        .build())
                .prepare()
                .executeAsBlocking()

        assertEquals(2, readedList.size.toLong())

        assertEquals(book1.id, readedList[0].id)
        assertEquals(book1.description, readedList[0].description)
        assertEquals(book1.title, readedList[0].title)
        assertEquals(book1.picture, readedList[0].picture)

        assertEquals(book2.id, readedList[1].id)
        assertEquals(book2.description, readedList[1].description)
        assertEquals(book2.title, readedList[1].title)
        assertEquals(book2.picture, readedList[1].picture)

    }

    @Test
    fun writeTwoItemsByThreeTimes_readByStorIo() {
        bookDb
                .put()
                .`object`(book1)
                .prepare()
                .executeAsBlocking()

        bookDb
                .put()
                .`object`(book2)
                .prepare()
                .executeAsBlocking()

        bookDb
                .put()
                .`object`(book1)
                .prepare()
                .executeAsBlocking()

        val readedList = bookDb
                .get()
                .listOfObjects(Book::class.java)
                .withQuery(Query.builder()
                        .table(BookTable.TABLE_NAME)
                        .orderBy(BookTable.ID)
                        .build())
                .prepare()
                .executeAsBlocking()

        assertEquals(2, readedList.size.toLong())


    }

    companion object {
        /*
        test data
        */

        private val book1 = Book(
                1111,
                "title one",
                "picture url one",
                "description one",
                null)

        private val book2 = Book(2222,
                "title two",
                "picture url two",
                "description two",
                null)
    }

}