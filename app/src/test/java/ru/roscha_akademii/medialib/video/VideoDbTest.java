package ru.roscha_akademii.medialib.video;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;

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
import ru.roscha_akademii.medialib.video.model.UnexistingVideoException;
import ru.roscha_akademii.medialib.video.model.VideoDbModule;
import ru.roscha_akademii.medialib.video.model.local.VideoDb;
import ru.roscha_akademii.medialib.video.model.remote.entity.Video;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,
        sdk = 21,
        manifest = "AndroidManifest.xml",
        application = RobolectricMdiaLibApplication.class,
        packageName = "ru.roscha_akademii.medialib")

public class VideoDbTest {
    private StorIOSQLite videoDbStorIoHelper;
    private String videoDbFileName;

    private VideoDb videoDb; // SUT

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