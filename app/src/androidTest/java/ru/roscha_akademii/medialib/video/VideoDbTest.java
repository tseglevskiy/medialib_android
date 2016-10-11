package ru.roscha_akademii.medialib.video;

import android.app.Instrumentation;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.Component;
import ru.roscha_akademii.medialib.TestScope;
import ru.roscha_akademii.medialib.common.AndroidModule;
import ru.roscha_akademii.medialib.common.DaggerMediaLibApplication_ApplicationComponent;
import ru.roscha_akademii.medialib.common.MediaLibApplication;
import ru.roscha_akademii.medialib.common.MockMediaLibApplication;
import ru.roscha_akademii.medialib.net.model.Video;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ru.roscha_akademii.medialib.video.VideoDbModule.VIDEO_DB;
import static ru.roscha_akademii.medialib.video.VideoDbModule.VIDEO_DB_FILENAME;

public class VideoDbTest {
    @Inject
    @Named(VIDEO_DB)
    StorIOSQLite videoDb; // SUT

    @Inject
    @Named(VIDEO_DB_FILENAME)
    String videoDbFileName;

    @Inject
    VideoDb videoDbSqliteHelper;

    /*
    boilerplate
     */

    @TestScope
    @Component(dependencies = MediaLibApplication.ApplicationComponent.class)
    interface TestApplicationComponent {
        void inject(VideoDbTest test);
    }

    @Before
    public void setUp() throws Exception {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();

        MockMediaLibApplication app
                = (MockMediaLibApplication) instrumentation.getTargetContext().getApplicationContext();

        MediaLibApplication.ApplicationComponent component = DaggerMediaLibApplication_ApplicationComponent
                .builder()
                .androidModule(new AndroidModule(app, null))
                .videoDbModule(new VideoDbModule() {
                    @Override
                    public String providesVideoDbFileName() {
                        return ""; // in-memory database for tests
                    }
                })
                .build();

        app.setComponent(component);

        TestApplicationComponent testComponent = DaggerVideoDbTest_TestApplicationComponent
                .builder()
                .applicationComponent(component)
                .build();

        testComponent.inject(this);

    }

    /*
    test data
     */
    private static Video video1 = new Video();
    static {
        video1.id = 1111;
        video1.description = "description one";
        video1.pictureUrl = "picture url one";
        video1.title = "title one";
        video1.videoUrl = "video url one";
    }

    private static Video video2 = new Video();
    static {
        video2.id = 2222;
        video2.description = "description two";
        video2.pictureUrl = "picture url two";
        video2.title = "title two";
        video2.videoUrl = "video url two";
    }

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
                .rawQuery("select * from " + VideoDb.VideoT.TABLE_NAME, null);

        assertEquals("added one object", 1, cursor.getCount());

        int desriptionIdx = cursor.getColumnIndex(VideoDb.VideoT.DESCRIPTION);
        int idIdx = cursor.getColumnIndex(VideoDb.VideoT.ID);
        int titleIdx = cursor.getColumnIndex(VideoDb.VideoT.TITLE);
        int videoUrlIdx = cursor.getColumnIndex(VideoDb.VideoT.VIDEO_URL);
        int pictureUrlIdx = cursor.getColumnIndex(VideoDb.VideoT.PICTURE_URL);

        assertTrue(cursor.moveToFirst());

        assertEquals(video1.id, cursor.getLong(idIdx));
        assertEquals(video1.description, cursor.getString(desriptionIdx));
        assertEquals(video1.title, cursor.getString(titleIdx));
        assertEquals(video1.videoUrl, cursor.getString(videoUrlIdx));
        assertEquals(video1.pictureUrl, cursor.getString(pictureUrlIdx));

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
                        .table(VideoDb.VideoT.TABLE_NAME)
                        .orderBy(VideoDb.VideoT.ID)
                        .build())
                .prepare()
                .executeAsBlocking();

        assertEquals(2, readedList.size());

        assertEquals(video1.id, readedList.get(0).id);
        assertEquals(video1.description, readedList.get(0).description);
        assertEquals(video1.title, readedList.get(0).title);
        assertEquals(video1.videoUrl, readedList.get(0).videoUrl);
        assertEquals(video1.pictureUrl, readedList.get(0).pictureUrl);

        assertEquals(video2.id, readedList.get(1).id);
        assertEquals(video2.description, readedList.get(1).description);
        assertEquals(video2.title, readedList.get(1).title);
        assertEquals(video2.videoUrl, readedList.get(1).videoUrl);
        assertEquals(video2.pictureUrl, readedList.get(1).pictureUrl);

    }
}