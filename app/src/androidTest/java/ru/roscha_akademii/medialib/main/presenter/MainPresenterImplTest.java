package ru.roscha_akademii.medialib.main.presenter;

import android.app.Instrumentation;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import dagger.Component;
import ru.roscha_akademii.medialib.TestScope;
import ru.roscha_akademii.medialib.common.AndroidModule;
import ru.roscha_akademii.medialib.common.ApplicationComponent;
import ru.roscha_akademii.medialib.common.DaggerApplicationComponent;
import ru.roscha_akademii.medialib.common.MockMediaLibApplication;
import ru.roscha_akademii.medialib.common.ServiceNavigator;
import ru.roscha_akademii.medialib.common.TimeProvider;
import ru.roscha_akademii.medialib.update.UpdateModule;
import ru.roscha_akademii.medialib.update.UpdateScheduler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MainPresenterImplTest {

    private MainPresenterImpl presenter; // SUT

    @TestScope
    @Component(dependencies = ApplicationComponent.class)
    interface MockApplicationComponent {
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

        ApplicationComponent component = DaggerApplicationComponent
                .builder()
                .androidModule(new AndroidModule(app, null))
                .updateModule(new UpdateModule() {
                    @Override
                    public UpdateScheduler provides(SharedPreferences prefs, TimeProvider timeProvider, ServiceNavigator navigator) {
                        return mock(UpdateScheduler.class);
                    }
                })
                .build();

        app.setComponent(component);

        MockApplicationComponent testComponent = DaggerMainPresenterImplTest_MockApplicationComponent
                .builder()
                .applicationComponent(component)
                .build();
        testComponent.inject(this);

        presenter = new MainPresenterImpl(context);
    }

    @Test
    public void start() throws Exception {
        presenter.start();

        verify(updateScheduler, times(1)).startBySchedule();

    }

}