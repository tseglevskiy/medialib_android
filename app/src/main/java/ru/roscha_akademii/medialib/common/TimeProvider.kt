package ru.roscha_akademii.medialib.common

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class TimeProvider @Inject
internal constructor() {

    /**
     * Текущее время
     * Технически -- это System.currentTimeMillis()

     * @return текущее время, в миллисекундах от начала эпохи
     */
    open fun currentTimeMillis(): Long {
        return System.currentTimeMillis()
    }
}
