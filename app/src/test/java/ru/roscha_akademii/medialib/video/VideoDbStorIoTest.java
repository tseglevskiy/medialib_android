package ru.roscha_akademii.medialib.video;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;

import ru.roscha_akademii.medialib.BuildConfig;
import ru.roscha_akademii.medialib.common.AndroidModule;
import ru.roscha_akademii.medialib.common.ApplicationComponent;
import ru.roscha_akademii.medialib.common.DaggerApplicationComponent;
import ru.roscha_akademii.medialib.common.RobolectricMdiaLibApplication;
import ru.roscha_akademii.medialib.video.model.VideoDbModule;
import ru.roscha_akademii.medialib.video.model.local.VideoDbSqliteHelper;
import ru.roscha_akademii.medialib.video.model.local.VideoTable;
import ru.roscha_akademii.medialib.video.model.remote.Video;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,
        sdk = 21,
        manifest = "AndroidManifest.xml",
        application = RobolectricMdiaLibApplication.class,
        packageName = "ru.roscha_akademii.medialib")

public class VideoDbStorIoTest {
    private StorIOSQLite videoDb; // SUT

    private String videoDbFileName;
    private VideoDbSqliteHelper videoDbSqliteHelper;

    /*
    boilerplate
     */

    @Before
    public void setUp() throws Exception {
        RobolectricMdiaLibApplication app = (RobolectricMdiaLibApplication) RuntimeEnvironment.application;

        ApplicationComponent component = DaggerApplicationComponent
                .builder()
                .androidModule(new AndroidModule(app, app.refWatcher))
                .videoDbModule(new VideoDbModule() {
                    @NonNull
                    @Override
                    public String providesVideoDbFileName() {
                        return ""; // in-memory database for tests
                    }
                })
                .build();

        app.setTestComponent(component);

        videoDb = component.videoDbStorIo(); // SUT

        videoDbFileName = component.videoDbFileName();
        videoDbSqliteHelper = component.videoDbSqliteHelper();
    }

    /*
    test data
     */
    private static Video video1 = new Video(
            1111,
            "title one",
            "picture url one",
            "description one",
            "video url one",
            LocalDate.parse("2000-01-01"),
            "0:01");

    private static Video video2 = new Video(2222,
            "title two",
            "picture url two",
            "description two",
            "video url two",
            LocalDate.parse("2001-02-02"),
            "0:02");

    /*
    tests
     */

    @Test
    public void environment_inMemoryDb() {
        assertTrue("db filename for test has to be empty string", videoDbFileName.isEmpty());
    }

    @Test
    public void writeOneItem_readManually() {
        videoDb
                .put()
                .object(video1)
                .prepare()
                .executeAsBlocking();

        Cursor cursor = videoDbSqliteHelper
                .getReadableDatabase()
                .rawQuery("select * from " + VideoTable.TABLE_NAME, null);

        assertEquals("added one object", 1, cursor.getCount());

        int desriptionIdx = cursor.getColumnIndex(VideoTable.DESCRIPTION);
        int idIdx = cursor.getColumnIndex(VideoTable.ID);
        int titleIdx = cursor.getColumnIndex(VideoTable.TITLE);
        int videoUrlIdx = cursor.getColumnIndex(VideoTable.VIDEO_URL);
        int pictureUrlIdx = cursor.getColumnIndex(VideoTable.PICTURE_URL);

        assertTrue(cursor.moveToFirst());

        assertEquals(video1.getId(), cursor.getLong(idIdx));
        assertEquals(video1.getDescription(), cursor.getString(desriptionIdx));
        assertEquals(video1.getTitle(), cursor.getString(titleIdx));
        assertEquals(video1.getVideoUrl(), cursor.getString(videoUrlIdx));
        assertEquals(video1.getPictureUrl(), cursor.getString(pictureUrlIdx));

        cursor.close();
    }

    @Test
    public void writeTwoItems_readByStorIo() {
        videoDb
                .put()
                .object(video1)
                .prepare()
                .executeAsBlocking();

        videoDb
                .put()
                .object(video2)
                .prepare()
                .executeAsBlocking();

        List<Video> readedList = videoDb
                .get()
                .listOfObjects(Video.class)
                .withQuery(Query.builder()
                        .table(VideoTable.TABLE_NAME)
                        .orderBy(VideoTable.ID)
                        .build())
                .prepare()
                .executeAsBlocking();

        assertEquals(2, readedList.size());

        assertEquals(video1.getId(), readedList.get(0).getId());
        assertEquals(video1.getDescription(), readedList.get(0).getDescription());
        assertEquals(video1.getTitle(), readedList.get(0).getTitle());
        assertEquals(video1.getVideoUrl(), readedList.get(0).getVideoUrl());
        assertEquals(video1.getPictureUrl(), readedList.get(0).getPictureUrl());

        assertEquals(video2.getId(), readedList.get(1).getId());
        assertEquals(video2.getDescription(), readedList.get(1).getDescription());
        assertEquals(video2.getTitle(), readedList.get(1).getTitle());
        assertEquals(video2.getVideoUrl(), readedList.get(1).getVideoUrl());
        assertEquals(video2.getPictureUrl(), readedList.get(1).getPictureUrl());

    }

    @Test
    public void writeTwoItemsByThreeTimes_readByStorIo() {
        videoDb
                .put()
                .object(video1)
                .prepare()
                .executeAsBlocking();

        videoDb
                .put()
                .object(video2)
                .prepare()
                .executeAsBlocking();

        videoDb
                .put()
                .object(video1)
                .prepare()
                .executeAsBlocking();

        List<Video> readedList = videoDb
                .get()
                .listOfObjects(Video.class)
                .withQuery(Query.builder()
                        .table(VideoTable.TABLE_NAME)
                        .orderBy(VideoTable.ID)
                        .build())
                .prepare()
                .executeAsBlocking();

        assertEquals(2, readedList.size());


    }

}