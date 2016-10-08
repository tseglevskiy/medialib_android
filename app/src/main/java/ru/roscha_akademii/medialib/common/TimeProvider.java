package ru.roscha_akademii.medialib.common;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TimeProvider {
    @Inject
    TimeProvider() {
    }

    /**
     * Текущее время
     * Технически -- это System.currentTimeMillis()
     *
     * @return текущее время, в миллисекундах от начала эпохи
     */
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
