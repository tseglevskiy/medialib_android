package ru.roscha_akademii.medialib.update;

import android.content.SharedPreferences;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.roscha_akademii.medialib.common.ServiceNavigator;
import ru.roscha_akademii.medialib.common.TimeProvider;

@Singleton
public class UpdateScheduler {
    private SharedPreferences prefs;
    private TimeProvider timeProvider;
    private ServiceNavigator navigator;

    private static final long UPDATE_TIMEOUT = TimeUnit.DAYS.toMillis(1);
    static final String PREFS_UPDATE_SCHEDULE = "PREFS_UPDATE_SCHEDULE";

    @Inject
    UpdateScheduler(SharedPreferences prefs,
                    TimeProvider timeProvider,
                    ServiceNavigator navigator)
    {
        this.prefs = prefs;
        this.timeProvider = timeProvider;
        this.navigator = navigator;
    }

    public void startBySchedule() {
        long last = prefs.getLong(PREFS_UPDATE_SCHEDULE, 0);
        long now = timeProvider.currentTimeMillis();
        if (now - last > UPDATE_TIMEOUT) {
            startNow();
        }
    }

    public void startNow() {
        navigator.startUpdate();
    }

    public void updateCompleted() {
        long now = timeProvider.currentTimeMillis();
        prefs.edit().putLong(PREFS_UPDATE_SCHEDULE, now).apply();
    }

}
