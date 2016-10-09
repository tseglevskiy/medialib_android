package ru.roscha_akademii.medialib.net;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class BaseUrlModule {
    public static final String BASE_URL = "https://bitbucket.org/tseglevskiy/medialib_android/";

    @Provides
    @Singleton
    @Named("base url")
    String baseUrl() {
        return BASE_URL;
    }
}
