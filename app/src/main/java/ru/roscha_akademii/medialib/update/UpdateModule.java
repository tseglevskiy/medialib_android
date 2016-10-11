package ru.roscha_akademii.medialib.update;

import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.roscha_akademii.medialib.common.ServiceNavigator;
import ru.roscha_akademii.medialib.common.TimeProvider;

@Module
public class UpdateModule {
    @Provides
    @Singleton
    public UpdateScheduler provides(SharedPreferences prefs,
                  TimeProvider timeProvider,
                  ServiceNavigator navigator) {
        return new UpdateScheduler(prefs, timeProvider, navigator);
    }
}
