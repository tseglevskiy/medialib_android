package ru.roscha_akademii.medialib.video;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import ru.roscha_akademii.medialib.common.AndroidModule;
import ru.roscha_akademii.medialib.common.ApplicationComponent;
import ru.roscha_akademii.medialib.common.DaggerApplicationComponent;
import ru.roscha_akademii.medialib.common.MockMediaLibApplication;
import ru.roscha_akademii.medialib.net.model.Video;

import static org.junit.Assert.*;

public class VideoDbTest {
    private StorIOSQLite videoDbStorIoHelper;
    private String videoDbFileName;

    private VideoDb videoDb; // SUT

    /*
    boilerplate
     */

    @Before
    public void setUp() throws Exception {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();

        MockMediaLibApplication app
                = (MockMediaLibApplication) instrumentation.getTargetContext().getApplicationContext();

        ApplicationComponent component = DaggerApplicationComponent
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

        videoDbStorIoHelper = component.videoDbStorIo();

        videoDbFileName = component.videoDbFileName();

        videoDb = component.videoDb(); // SUT
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
    public void emptyDb_readAllVideo() {
        List<Video> list = videoDb.getAllVideo();

        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void twoVideos_readAllVideo() {
        videoDbStorIoHelper
                .put()
                .object(video1)
                .prepare()
                .executeAsBlocking();

        videoDbStorIoHelper
                .put()
                .object(video2)
                .prepare()
                .executeAsBlocking();

        List<Video> list = videoDb.getAllVideo();

        assertNotNull(list);
        assertEquals(2, list.size());

        assertEquals(video1.id, list.get(0).id);
        assertEquals(video2.id, list.get(1).id);
    }

    @Test
    public void twoVideos_readVideo_unexistingVideo() {
        videoDbStorIoHelper
                .put()
                .object(video1)
                .prepare()
                .executeAsBlocking();

        videoDbStorIoHelper
                .put()
                .object(video2)
                .prepare()
                .executeAsBlocking();

        long unexistingId = 3333;
        assertNotEquals(unexistingId, video1.id);
        assertNotEquals(unexistingId, video2.id);

        Video video = videoDb.getVideo(1999);

        assertNull(video);
    }

    @Test
    public void twoVideos_readVideo_oneVideo() {
        videoDbStorIoHelper
                .put()
                .object(video1)
                .prepare()
                .executeAsBlocking();

        videoDbStorIoHelper
                .put()
                .object(video2)
                .prepare()
                .executeAsBlocking();

        Video video = videoDb.getVideo(video1.id);

        assertEquals(video1.id, video.id);
        assertEquals(video1.description, video.description);

        video = videoDb.getVideo(video2.id);

        assertEquals(video2.id, video.id);
        assertEquals(video2.description, video.description);

    }
}