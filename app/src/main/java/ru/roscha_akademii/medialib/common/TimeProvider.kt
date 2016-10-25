package ru.roscha_akademii.medialib.common

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimeProvider @Inject
internal constructor() {

    /**
     * Текущее время
     * Технически -- это System.currentTimeMillis()

     * @return текущее время, в миллисекундах от начала эпохи
     */
    fun currentTimeMillis(): Long {
        return System.currentTimeMillis()
    }
}
