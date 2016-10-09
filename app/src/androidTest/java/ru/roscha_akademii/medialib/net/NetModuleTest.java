package ru.roscha_akademii.medialib.net;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Call;
import retrofit2.Response;
import ru.roscha_akademii.medialib.AssertsFileHelper;
import ru.roscha_akademii.medialib.common.AndroidModule;
import ru.roscha_akademii.medialib.common.MediaLibApplication;
import ru.roscha_akademii.medialib.common.MockMediaLibApplication;
import ru.roscha_akademii.medialib.net.model.Video;
import ru.roscha_akademii.medialib.net.model.VideoAnswer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NetModuleTest {
    private String testVideoList;
    private MockWebServer server;

    @Inject
    @Named("base url")
    String injectedBaseUrl;

    @Inject
    VideoApi videoApi;

    @Module
    class MockBaseUrlModule {
        String baseUrl;

        MockBaseUrlModule(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        @Provides
        @Singleton
        @Named("base url")
        String baseUrl() {
            return baseUrl;
        }
    }

    @Singleton
    @Component(modules = {
            AndroidModule.class,
            NetModule.class,
            MockBaseUrlModule.class
    })
    interface MockApplicationComponent extends MediaLibApplication.ApplicationComponent {
        void inject(NetModuleTest test);
    }

    @Before
    public void setUp() throws Exception {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();

        testVideoList = AssertsFileHelper.readFileFromAsserts(instrumentation, "video.json");

        MockMediaLibApplication app
                = (MockMediaLibApplication) instrumentation.getTargetContext().getApplicationContext();

        server = new MockWebServer();
        server.enqueue(new MockResponse().setBody(testVideoList));
        server.start();

        HttpUrl baseUrl = server.url("/tseglevskiy/medialib_android/");

        MockBaseUrlModule urlModule = new MockBaseUrlModule(baseUrl.toString());

        MockApplicationComponent component = DaggerNetModuleTest_MockApplicationComponent
                .builder()
                .androidModule(new AndroidModule(app, null))
                .mockBaseUrlModule(urlModule)
                .build();

        app.setComponent(component);
        component.inject(this);

    }

    @Test
    public void assetsFileExists() {
        assertTrue("test file should be exists", testVideoList.length() > 10);
    }

    @Test
    public void webServerMocked() {
        assertFalse("web server didn't mocked", injectedBaseUrl.equals(BaseUrlModule.BASE_URL));
    }

    @Test
    public void getListOfVideos() throws IOException, InterruptedException {
        Call<VideoAnswer> call = videoApi.getVideoList();
        Response<VideoAnswer> response = call.execute();

        assertTrue(response.isSuccessful());
        assertEquals(200, response.code());

        RecordedRequest request1 = server.takeRequest();
        assertEquals("/tseglevskiy/medialib_android/raw/87aff1ef294055d83b429a29c981b9514211a8bc/app/src/androidTest/assets/video.json", request1.getPath());

        ArrayList<Video> videos = response.body().list;
        assertEquals(2, videos.size());

        Video v1 = videos.get(0);
        assertEquals("название один", v1.title);
        assertEquals("http://jsonparsing.parseapp.com/jsonData/images/avengers.jpg", v1.pictureUrl);
        assertEquals("описание один", v1.description);
    }

}