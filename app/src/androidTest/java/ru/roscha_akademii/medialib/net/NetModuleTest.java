package ru.roscha_akademii.medialib.net;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;

import com.squareup.leakcanary.RefWatcher;

import org.jetbrains.annotations.NotNull;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Call;
import retrofit2.Response;
import ru.roscha_akademii.medialib.AssertsFileHelper;
import ru.roscha_akademii.medialib.common.AndroidModule;
import ru.roscha_akademii.medialib.common.ApplicationComponent;
import ru.roscha_akademii.medialib.common.DaggerApplicationComponent;
import ru.roscha_akademii.medialib.common.MockMediaLibApplication;
import ru.roscha_akademii.medialib.video.model.remote.Video;
import ru.roscha_akademii.medialib.video.model.remote.VideoAnswer;
import ru.roscha_akademii.medialib.video.model.remote.VideoApi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class NetModuleTest {
    private String testVideoList;
    private MockWebServer server;

    private VideoApi videoApi; // SUT

    private String injectedBaseUrl;

    @Before
    public void setUp() throws Exception {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();

        testVideoList = AssertsFileHelper.readFileFromAsserts(instrumentation, "video.json");

        MockMediaLibApplication app
                = (MockMediaLibApplication) instrumentation.getTargetContext().getApplicationContext();

        server = new MockWebServer();
        server.enqueue(new MockResponse().setBody(testVideoList));
        server.start();

        final HttpUrl baseUrl = server.url("/tseglevskiy/medialib_android/");

        ApplicationComponent component = DaggerApplicationComponent
                .builder()
                .androidModule(new AndroidModule(app, app.refWatcher))
                .netModule(new NetModule() {
                    @NotNull
                    @Override
                    public String baseUrl() {
                        return baseUrl.toString();
                    }
                })
                .build();

        app.setTestComponent(component);

        videoApi = component.videoApi();
        injectedBaseUrl = component.serverBaseUrl();

    }

    @Test
    public void assetsFileExists() {
        assertTrue("test file should be exists", testVideoList.length() > 10);
    }

    @Test
    public void webServerMocked() {
        assertFalse("web server didn't mocked", injectedBaseUrl.equals(NetModule.Companion.getURL()));
    }

    @Test
    public void getListOfVideos() throws IOException, InterruptedException {
        Call<VideoAnswer> call = videoApi.videoList();
        Response<VideoAnswer> response = call.execute();

        assertTrue(response.isSuccessful());
        assertEquals(200, response.code());

        RecordedRequest request1 = server.takeRequest();
        assertEquals("/tseglevskiy/medialib_android/app/src/androidTest/assets/video.json", request1.getPath());

        ArrayList<Video> videos = response.body().getList();
        assertNotNull(videos);
        assertEquals(3, videos.size());

        Video v1 = videos.get(0);
        assertEquals(13, v1.getId());
        assertEquals("Мазыкская игрушка. Елочка", v1.getTitle());
        assertEquals("http://video.roscha-akademii.ru/img/movie/preview/13.jpg", v1.getPictureUrl());
        assertEquals("http://video.roscha-akademii.ru/img/movie/video/video1.mp4", v1.getVideoUrl());
        assertTrue(v1.getDescription().length() > 10);
        assertEquals("0:43 мин.", v1.getDuration());
        assertEquals(LocalDate.parse("2016-02-16"), v1.getIssueDate());
    }

}