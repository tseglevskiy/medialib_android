package ru.roscha_akademii.medialib.update;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import ru.roscha_akademii.medialib.common.ServiceNavigator;
import ru.roscha_akademii.medialib.common.TimeProvider;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UpdateSchedulerTest {
    private UpdateScheduler scheduler; // SUT

    private SharedPreferences prefs;
    private TimeProvider timeProvider;
    private ServiceNavigator navigator;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    @Before
    public void setUp() throws Exception {

        editor = mock(SharedPreferences.Editor.class);
        when(editor.putLong(anyString(), anyLong())).thenReturn(editor);
        prefs = mock(SharedPreferences.class);
        when(prefs.edit()).thenReturn(editor);

        timeProvider = mock(TimeProvider.class);
        navigator = mock(ServiceNavigator.class);

        scheduler = new UpdateScheduler(prefs, timeProvider, navigator);
    }

    @Test
    public void startByScheduleFirstTime() throws Exception {
        long NOW = 100000000;
        when(timeProvider.currentTimeMillis()).thenReturn(NOW);
        when(prefs.getLong(anyString(), anyLong())).thenReturn(0L);

        scheduler.startBySchedule();

        verify(navigator, times(1)).startUpdate();

    }

    @Test
    public void startByScheduleShortTimeAgo() throws Exception {
        long NOW = 1000000000;
        long PERIOD = 24* 60 * 60 * 1000;
        when(timeProvider.currentTimeMillis()).thenReturn(NOW);
        when(prefs.getLong(anyString(), anyLong())).thenReturn(NOW - PERIOD + 2);

        scheduler.startBySchedule();

        verify(navigator, times(0)).startUpdate();

    }

    @Test
    public void startByScheduleLongTimeAgo() throws Exception {
        long NOW = 1000000000;
        long PERIOD = 24* 60 * 60 * 1000;
        when(timeProvider.currentTimeMillis()).thenReturn(NOW);
        when(prefs.getLong(anyString(), anyLong())).thenReturn(NOW - PERIOD - 2);

        scheduler.startBySchedule();

        verify(navigator, times(1)).startUpdate();

    }

    @Test
    public void startNow() throws Exception {
        scheduler.startNow();

        verify(navigator, times(1)).startUpdate();
    }

    @Test
    public void updateCompleted() throws Exception {
        long NOW = 1000000;
        when(timeProvider.currentTimeMillis()).thenReturn(NOW);

        scheduler.updateCompleted();

        ArgumentCaptor<Long> captorForTime = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> captorForKey = ArgumentCaptor.forClass(String.class);

        verify(editor, times(1))
                .putLong(captorForKey.capture(), captorForTime.capture());
        verify(editor, times(1)).apply();

        assertEquals(NOW, (long)captorForTime.getAllValues().get(0));
        assertEquals(
                UpdateScheduler.Constants.getPREFS_UPDATE_SCHEDULE(),
                captorForKey.getAllValues().get(0));
    }

}