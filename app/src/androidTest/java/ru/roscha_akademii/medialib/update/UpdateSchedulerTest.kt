package ru.roscha_akademii.medialib.update

import android.annotation.SuppressLint
import android.content.SharedPreferences

import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor

import ru.roscha_akademii.medialib.common.ServiceNavigator
import ru.roscha_akademii.medialib.common.TimeProvider

import org.junit.Assert.assertEquals
import org.mockito.Matchers.anyLong
import org.mockito.Matchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import ru.roscha_akademii.medialib.whenever

class UpdateSchedulerTest {
    lateinit var scheduler: UpdateScheduler // SUT

    lateinit var prefs: SharedPreferences
    lateinit var timeProvider: TimeProvider
    lateinit var navigator: ServiceNavigator
    lateinit var editor: SharedPreferences.Editor

    @SuppressLint("CommitPrefEdits")
    @Before
    @Throws(Exception::class)
    fun setUp() {

        editor = mock(SharedPreferences.Editor::class.java)

        whenever(editor.putLong(anyString(), anyLong())).thenReturn(editor)
        prefs = mock(SharedPreferences::class.java)
        whenever(prefs.edit()).thenReturn(editor)

        timeProvider = mock(TimeProvider::class.java)
        navigator = mock(ServiceNavigator::class.java)

        scheduler = UpdateScheduler(prefs, timeProvider, navigator)
    }

    @Test
    @Throws(Exception::class)
    fun startByScheduleFirstTime() {
        val NOW: Long = 100000000
        whenever(timeProvider.currentTimeMillis()).thenReturn(NOW)
        whenever(prefs.getLong(anyString(), anyLong())).thenReturn(0L)

        scheduler.startBySchedule()

        verify(navigator, times(1)).startUpdate()

    }

    @Test
    @Throws(Exception::class)
    fun startByScheduleShortTimeAgo() {
        val NOW: Long = 1000000000
        val PERIOD = 24 * 60 * 60 * 1000.toLong()
        whenever(timeProvider.currentTimeMillis()).thenReturn(NOW)
        whenever(prefs.getLong(anyString(), anyLong())).thenReturn(NOW - PERIOD + 2)

        scheduler.startBySchedule()

        verify(navigator, times(0)).startUpdate()

    }

    @Test
    @Throws(Exception::class)
    fun startByScheduleLongTimeAgo() {
        val NOW: Long = 1000000000
        val PERIOD = 24 * 60 * 60 * 1000.toLong()
        whenever(timeProvider.currentTimeMillis()).thenReturn(NOW)
        whenever(prefs.getLong(anyString(), anyLong())).thenReturn(NOW - PERIOD - 2)

        scheduler.startBySchedule()

        verify(navigator, times(1)).startUpdate()

    }

    @Test
    @Throws(Exception::class)
    fun startNow() {
        scheduler.startNow()

        verify(navigator, times(1)).startUpdate()
    }

    @Test
    @Throws(Exception::class)
    fun updateCompleted() {
        val NOW: Long = 1000000
        whenever(timeProvider.currentTimeMillis()).thenReturn(NOW)

        scheduler.updateCompleted()

        val captorForTime = ArgumentCaptor.forClass(Long::class.java)
        val captorForKey = ArgumentCaptor.forClass(String::class.java)

        verify(editor, times(1))
                .putLong(captorForKey.capture(), captorForTime.capture())
        verify(editor, times(1)).apply()

        assertEquals(NOW, captorForTime.allValues[0] as Long)
        assertEquals(
                UpdateScheduler.PREFS_UPDATE_SCHEDULE,
                captorForKey.allValues[0])
    }

}