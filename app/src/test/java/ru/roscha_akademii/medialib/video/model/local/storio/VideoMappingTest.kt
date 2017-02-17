package ru.roscha_akademii.medialib.video.model.local.storio

import android.database.MatrixCursor
import org.joda.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import ru.roscha_akademii.medialib.BuildConfig
import ru.roscha_akademii.medialib.common.RobolectricMdiaLibApplication
import ru.roscha_akademii.medialib.video.model.local.VideoTable
import ru.roscha_akademii.medialib.video.model.remote.entity.Video

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class,
        sdk = intArrayOf(21),
        manifest = "AndroidManifest.xml",
        application = RobolectricMdiaLibApplication::class,
        packageName = "ru.roscha_akademii.medialib")

class VideoMappingTest {
    lateinit var mapping: VideoMapping
    lateinit internal var putResolver: VideoMapping.PutResolver
    lateinit internal var deleteResolver: VideoMapping.DeleteResolver
    lateinit internal var getResolver: VideoMapping.GetResolver

    val video1 = Video(
            id = 1111,
            title = "title one",
            pictureUrl = "picture url one",
            description = "description one",
            videoUrl = "video url one",
            duration = "0:05",
            issueDate = LocalDate.parse("2000-01-02"))

    @Before
    fun setUp() {
        mapping = VideoMapping()
        putResolver = mapping.putResolver() as VideoMapping.PutResolver
        getResolver = mapping.getResolver() as VideoMapping.GetResolver
        deleteResolver = mapping.deleteResolver() as VideoMapping.DeleteResolver
    }

    @Test
    fun mapToUpdateQuery_identifyVideoById() {
        val query = putResolver.mapToUpdateQuery(video1)

        assertEquals(VideoTable.TABLE_NAME, query.table())

        assertEquals("${VideoTable.ID}=?", query.where().replace(" ", "", true))

        assertEquals(1, query.whereArgs().size)
        assertEquals(video1.id, query.whereArgs()[0].toLong())
    }

    @Test
    fun mapToContentValues() {
        val values = putResolver.mapToContentValues(video1)

        assertEquals(7, values.size())
        assertEquals(video1.id, values.get(VideoTable.ID))
        assertEquals(video1.videoUrl, values.get(VideoTable.VIDEO_URL))
        assertEquals(video1.title, values.get(VideoTable.TITLE))
        assertEquals(video1.description, values.get(VideoTable.DESCRIPTION))
        assertEquals(video1.pictureUrl, values.get(VideoTable.PICTURE_URL))
        assertEquals(video1.duration, values.get(VideoTable.DURATION))
        assertEquals(video1.issueDate.toString(), values.get(VideoTable.DATE))
    }

    @Test
    fun mapToInsertQuery() {
        val query = putResolver.mapToInsertQuery(video1)

        assertEquals(VideoTable.TABLE_NAME, query.table())
    }

    @Test
    fun mapFromCursor() {
        val cursor = MatrixCursor(arrayOf(
                VideoTable.ID,
                VideoTable.TITLE,
                VideoTable.DESCRIPTION,
                VideoTable.VIDEO_URL,
                VideoTable.PICTURE_URL,
                VideoTable.DURATION,
                VideoTable.DATE
        ))

        cursor.newRow()
                .add(video1.id)
                .add(video1.title)
                .add(video1.description)
                .add(video1.videoUrl)
                .add(video1.pictureUrl)
                .add(video1.duration)
                .add(video1.issueDate.toString())
        cursor.moveToFirst()

        val video = getResolver.mapFromCursor(cursor)

        assertEquals(video1, video)
    }

    @Test
    fun mapToDeleteQuery() {
        val query = deleteResolver.mapToDeleteQuery(video1)

        assertEquals(VideoTable.TABLE_NAME, query.table())

        assertEquals("${VideoTable.ID}=?", query.where().replace(" ", "", true))

        assertEquals(1, query.whereArgs().size)
        assertEquals(video1.id, query.whereArgs()[0].toLong())
    }

}