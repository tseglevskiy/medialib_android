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
import ru.roscha_akademii.medialib.book.model.remote.entity.Book
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
    fun twoVideos_readAllBooks() {
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
    fun saveZeroVideos() {
        bookDb.saveBooks(ArrayList<Book>())

        val list = bookDb.allBooks
        assertNotNull(list)
        assertEquals(0, list.size.toLong())
    }

    @Test
    fun saveTwoVideos() {

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

//    @Test
//    fun twoVideos_readVideo_unexistingVideo() {
//        bookDbStorIoHelper
//                .put()
//                .`object`(book1)
//                .prepare()
//                .executeAsBlocking()
//
//        bookDbStorIoHelper
//                .put()
//                .`object`(book2)
//                .prepare()
//                .executeAsBlocking()
//
//        val unexistingId: Long = 3333
//        assertNotEquals(unexistingId, book1.id)
//        assertNotEquals(unexistingId, book2.id)
//
//        try {
//            bookDb.getVideo(1999)
//            fail("taking unexisting video has to throw UnexistingVideoException")
//        } catch (e: UnexistingVideoException) {
//            // OK
//        }
//
//    }

//    @Test
//    fun twoVideos_readVideo_oneVideo() {
//        bookDbStorIoHelper
//                .put()
//                .`object`(book1)
//                .prepare()
//                .executeAsBlocking()
//
//        bookDbStorIoHelper
//                .put()
//                .`object`(book2)
//                .prepare()
//                .executeAsBlocking()
//
//        var video = bookDb.getVideo(book1.id)
//
//        assertEquals(book1.id, video.id)
//        assertEquals(book1.description, video.description)
//
//        video = bookDb.getVideo(book2.id)
//
//        assertEquals(book2.id, video.id)
//        assertEquals(book2.description, video.description)
//
//    }

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

        private val book2 = Book(
                2222,
                "title two",
                "picture url two",
                "description two",
                null)
    }
}