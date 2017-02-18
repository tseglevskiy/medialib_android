package ru.roscha_akademii.medialib.video.model.local

import com.pushtorefresh.storio.sqlite.StorIOSQLite
import com.pushtorefresh.storio.sqlite.queries.Query
import org.joda.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import ru.roscha_akademii.medialib.BuildConfig
import ru.roscha_akademii.medialib.common.AndroidModule
import ru.roscha_akademii.medialib.common.DaggerApplicationComponent
import ru.roscha_akademii.medialib.common.RobolectricMdiaLibApplication
import ru.roscha_akademii.medialib.video.model.VideoDbModule
import ru.roscha_akademii.medialib.video.model.remote.entity.Video

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class,
        sdk = intArrayOf(21),
        manifest = "AndroidManifest.xml",
        application = RobolectricMdiaLibApplication::class,
        packageName = "ru.roscha_akademii.medialib")

class VideoDbStorIoTest {
    lateinit var videoDb: StorIOSQLite // SUT

    lateinit var videoDbFileName: String
    lateinit var videoDbSqliteHelper: VideoDbSqliteHelper

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
                .videoDbModule(object : VideoDbModule() {
                    override fun providesVideoDbFileName(): String {
                        return "" // in-memory database for tests
                    }
                })
                .build()

        app.setTestComponent(component)

        videoDb = component.videoDbStorIo() // SUT

        videoDbFileName = component.videoDbFileName()
        videoDbSqliteHelper = component.videoDbSqliteHelper()
    }

    /*
    tests
     */

    @Test
    fun environment_inMemoryDb() {
        assertTrue("db filename for test has to be empty string", videoDbFileName.isEmpty())
    }

    @Test
    fun writeOneItem_readManually() {
        videoDb
                .put()
                .`object`(video1)
                .prepare()
                .executeAsBlocking()

        val cursor = videoDbSqliteHelper
                .readableDatabase
                .rawQuery("select * from " + VideoTable.TABLE_NAME, null)

        assertEquals("added one object", 1, cursor.count.toLong())

        val desriptionIdx = cursor.getColumnIndex(VideoTable.DESCRIPTION)
        val idIdx = cursor.getColumnIndex(VideoTable.ID)
        val titleIdx = cursor.getColumnIndex(VideoTable.TITLE)
        val videoUrlIdx = cursor.getColumnIndex(VideoTable.VIDEO_URL)
        val pictureUrlIdx = cursor.getColumnIndex(VideoTable.PICTURE_URL)

        assertTrue(cursor.moveToFirst())

        assertEquals(video1.id, cursor.getLong(idIdx))
        assertEquals(video1.description, cursor.getString(desriptionIdx))
        assertEquals(video1.title, cursor.getString(titleIdx))
        assertEquals(video1.videoUrl, cursor.getString(videoUrlIdx))
        assertEquals(video1.pictureUrl, cursor.getString(pictureUrlIdx))

        cursor.close()
    }

    @Test
    fun writeTwoItems_readByStorIo() {
        videoDb
                .put()
                .`object`(video1)
                .prepare()
                .executeAsBlocking()

        videoDb
                .put()
                .`object`(video2)
                .prepare()
                .executeAsBlocking()

        val readedList = videoDb
                .get()
                .listOfObjects(Video::class.java)
                .withQuery(Query.builder()
                        .table(VideoTable.TABLE_NAME)
                        .orderBy(VideoTable.ID)
                        .build())
                .prepare()
                .executeAsBlocking()

        assertEquals(2, readedList.size.toLong())

        assertEquals(video1.id, readedList[0].id)
        assertEquals(video1.description, readedList[0].description)
        assertEquals(video1.title, readedList[0].title)
        assertEquals(video1.videoUrl, readedList[0].videoUrl)
        assertEquals(video1.pictureUrl, readedList[0].pictureUrl)

        assertEquals(video2.id, readedList[1].id)
        assertEquals(video2.description, readedList[1].description)
        assertEquals(video2.title, readedList[1].title)
        assertEquals(video2.videoUrl, readedList[1].videoUrl)
        assertEquals(video2.pictureUrl, readedList[1].pictureUrl)

    }

    @Test
    fun writeTwoItemsByThreeTimes_readByStorIo() {
        videoDb
                .put()
                .`object`(video1)
                .prepare()
                .executeAsBlocking()

        videoDb
                .put()
                .`object`(video2)
                .prepare()
                .executeAsBlocking()

        videoDb
                .put()
                .`object`(video1)
                .prepare()
                .executeAsBlocking()

        val readedList = videoDb
                .get()
                .listOfObjects(Video::class.java)
                .withQuery(Query.builder()
                        .table(VideoTable.TABLE_NAME)
                        .orderBy(VideoTable.ID)
                        .build())
                .prepare()
                .executeAsBlocking()

        assertEquals(2, readedList.size.toLong())


    }

    companion object {

        /*
    test data
     */
        private val video1 = Video(
                1111,
                "title one",
                "picture url one",
                "description one",
                "video url one",
                LocalDate.parse("2000-01-01"),
                "0:01")

        private val video2 = Video(2222,
                "title two",
                "picture url two",
                "description two",
                "video url two",
                LocalDate.parse("2001-02-02"),
                "0:02")
    }

}