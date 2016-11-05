package ru.roscha_akademii.medialib.video.model.local.storio

import android.database.MatrixCursor
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ru.roscha_akademii.medialib.video.model.local.VideoTable
import ru.roscha_akademii.medialib.video.model.remote.Video

class VideoMappingTest {
    lateinit var mapping: VideoMapping
    lateinit internal var putResolver: VideoMapping.PutResolver
    lateinit internal var deleteResolver: VideoMapping.DeleteResolver
    lateinit internal var getResolver: VideoMapping.GetResolver

    val video1 = Video(
            1111,
            "title one",
            "picture url one",
            "description one",
            "video url one")

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

        assertEquals(5, values.size())
        assertEquals(video1.id, values.get(VideoTable.ID))
        assertEquals(video1.videoUrl, values.get(VideoTable.VIDEO_URL))
        assertEquals(video1.title, values.get(VideoTable.TITLE))
        assertEquals(video1.description, values.get(VideoTable.DESCRIPTION))
        assertEquals(video1.pictureUrl, values.get(VideoTable.PICTURE_URL))
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
                VideoTable.PICTURE_URL
        ))
        val row = cursor.newRow()
                .add(video1.id)
                .add(video1.title)
                .add(video1.description)
                .add(video1.videoUrl)
                .add(video1.pictureUrl)
        cursor.moveToFirst()

        val video = getResolver.mapFromCursor(cursor)

        assert(video1.equals(video))
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