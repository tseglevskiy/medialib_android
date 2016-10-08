package ru.roscha_akademii.medialib.main.presenter;

import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import ru.roscha_akademii.medialib.common.AndroidModule;
import ru.roscha_akademii.medialib.common.MediaLibApplication;
import ru.roscha_akademii.medialib.common.MockMediaLibApplication;
import ru.roscha_akademii.medialib.update.UpdateScheduler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MainPresenterImplTest {

    private MainPresenterImpl presenter; // SUT

    @Module
    class MockUpdateSchedulerModule {

        @Provides
        @Singleton
        UpdateScheduler providesUpdateScheduler() {
            return mock(UpdateScheduler.class);
        }
    }

    @Singleton
    @Component(modules = {
            AndroidModule.class,
            MockUpdateSchedulerModule.class})
    interface MockApplicationComponent extends MediaLibApplication.ApplicationComponent {
        void inject(MainPresenterImplTest test);
    }

    @Inject
    UpdateScheduler updateScheduler;

    @Inject
    Context context;

    @Before
    public void setUp() throws Exception {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();

        MockMediaLibApplication app
                = (MockMediaLibApplication) instrumentation.getTargetContext().getApplicationContext();

        MockApplicationComponent component = DaggerMainPresenterImplTest_MockApplicationComponent
                .builder()
                .androidModule(new AndroidModule(app, null))
                .mockUpdateSchedulerModule(new MockUpdateSchedulerModule())
                .build();

        app.setComponent(component);
        component.inject(this);

        presenter = new MainPresenterImpl(context);
    }

    @Test
    public void start() throws Exception {
        presenter.start();

        verify(updateScheduler, times(1)).startBySchedule();

    }

}