package ru.roscha_akademii.medialib.main.presenter;

import org.junit.Before;
import org.junit.Test;

import ru.roscha_akademii.medialib.update.UpdateScheduler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MainPresenterImplTest {

    private MainPresenterImpl presenter; // SUT

    private UpdateScheduler updateScheduler;

    @Before
    public void setUp() throws Exception {
        updateScheduler = mock(UpdateScheduler.class);

        presenter = new MainPresenterImpl(updateScheduler);
    }

    @Test
    public void start() throws Exception {
        presenter.start();

        verify(updateScheduler, times(1)).startBySchedule();
    }

}