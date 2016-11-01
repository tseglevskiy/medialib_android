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
import ru.roscha_akademii.medialib.video.model.remote.Video;
import ru.roscha_akademii.medialib.video.model.UnexistingVideoException;
import ru.roscha_akademii.medialib.video.model.local.VideoDb;
import ru.roscha_akademii.medialib.video.model.VideoDbModule;

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

        app.setTestComponent(component);

        videoDbStorIoHelper = component.videoDbStorIo();

        videoDbFileName = component.videoDbFileName();

        videoDb = component.videoDb(); // SUT
    }

    /*
    test data
     */
    private static Video video1 = new Video();

    static {
        video1.setId(1111);
        video1.setDescription("description one");
        video1.setPictureUrl("picture url one");
        video1.setTitle("title one");
        video1.setVideoUrl("video url one");
    }

    private static Video video2 = new Video();

    static {
        video2.setId(2222);
        video2.setDescription("description two");
        video2.setPictureUrl("picture url two");
        video2.setTitle("title two");
        video2.setVideoUrl("video url two");
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

        assertEquals(video1.getId(), list.get(0).getId());
        assertEquals(video2.getId(), list.get(1).getId());
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
        assertNotEquals(unexistingId, video1.getId());
        assertNotEquals(unexistingId, video2.getId());

        try {
            videoDb.getVideo(1999);
            fail("taking unexisting video has to throw UnexistingVideoException");
        } catch (UnexistingVideoException e) {
            // OK
        }
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

        Video video = videoDb.getVideo(video1.getId());

        assertEquals(video1.getId(), video.getId());
        assertEquals(video1.getDescription(), video.getDescription());

        video = videoDb.getVideo(video2.getId());

        assertEquals(video2.getId(), video.getId());
        assertEquals(video2.getDescription(), video.getDescription());

    }
}