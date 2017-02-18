package ru.roscha_akademii.medialib.video

import com.pushtorefresh.storio.sqlite.StorIOSQLite
import org.joda.time.LocalDate
import org.junit.Assert.*
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
import ru.roscha_akademii.medialib.video.model.UnexistingVideoException
import ru.roscha_akademii.medialib.video.model.VideoDbModule
import ru.roscha_akademii.medialib.video.model.local.VideoDb
import ru.roscha_akademii.medialib.video.model.remote.entity.Video
import java.util.*

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class,
        sdk = intArrayOf(21),
        manifest = "AndroidManifest.xml",
        application = RobolectricMdiaLibApplication::class,
        packageName = "ru.roscha_akademii.medialib")

class VideoDbTest {
    lateinit var videoDbStorIoHelper: StorIOSQLite
    lateinit var videoDbFileName: String

    lateinit var videoDb: VideoDb // SUT

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

        videoDbStorIoHelper = component.videoDbStorIo()

        videoDbFileName = component.videoDbFileName()

        videoDb = component.videoDb() // SUT
    }

    /*
    tests
     */

    @Test
    fun environment_inMemoryDb() {
        assertTrue("db filename for test has to be empty string", videoDbFileName.isEmpty())
    }

    @Test
    fun emptyDb_readAllVideo() {
        val list = videoDb.allVideo

        assertNotNull(list)
        assertEquals(0, list.size.toLong())
    }

    @Test
    fun twoVideos_readAllVideo() {
        videoDbStorIoHelper
                .put()
                .`object`(video1)
                .prepare()
                .executeAsBlocking()

        videoDbStorIoHelper
                .put()
                .`object`(video2)
                .prepare()
                .executeAsBlocking()

        val list = videoDb.allVideo

        assertNotNull(list)
        assertEquals(2, list.size.toLong())

        assertEquals(video1.id, list[0].id)
        assertEquals(video2.id, list[1].id)
    }

    @Test
    fun saveZeroVideos() {
        videoDb.saveVideos(ArrayList<Video>())

        val list = videoDb.allVideo
        assertNotNull(list)
        assertEquals(0, list.size.toLong())
    }

    @Test
    fun saveTwoVideos() {

        val listToSave = ArrayList<Video>()
        listToSave.add(video1)
        listToSave.add(video2)

        videoDb.saveVideos(listToSave)

        val list = videoDb.allVideo
        assertNotNull(list)
        assertEquals(2, list.size.toLong())

        assertEquals(video1.id, list[0].id)
        assertEquals(video2.id, list[1].id)
    }

    @Test
    fun twoVideos_readVideo_unexistingVideo() {
        videoDbStorIoHelper
                .put()
                .`object`(video1)
                .prepare()
                .executeAsBlocking()

        videoDbStorIoHelper
                .put()
                .`object`(video2)
                .prepare()
                .executeAsBlocking()

        val unexistingId: Long = 3333
        assertNotEquals(unexistingId, video1.id)
        assertNotEquals(unexistingId, video2.id)

        try {
            videoDb.getVideo(1999)
            fail("taking unexisting video has to throw UnexistingVideoException")
        } catch (e: UnexistingVideoException) {
            // OK
        }

    }

    @Test
    fun twoVideos_readVideo_oneVideo() {
        videoDbStorIoHelper
                .put()
                .`object`(video1)
                .prepare()
                .executeAsBlocking()

        videoDbStorIoHelper
                .put()
                .`object`(video2)
                .prepare()
                .executeAsBlocking()

        var video = videoDb.getVideo(video1.id)

        assertEquals(video1.id, video.id)
        assertEquals(video1.description, video.description)

        video = videoDb.getVideo(video2.id)

        assertEquals(video2.id, video.id)
        assertEquals(video2.description, video.description)

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