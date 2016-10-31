package ru.roscha_akademii.medialib.video.model.local

import com.pushtorefresh.storio.sqlite.StorIOSQLite

class VideoStorage(internal var db: StorIOSQLite) {
    fun getStatus(id: Long): StorageStatus {
        return StorageStatus.PROGRESS
    }

    fun getPercent(id: Long): Int {
        return when (getStatus(id)) {
            StorageStatus.LOCAL -> 100

            StorageStatus.PROGRESS -> 20

            else -> 0
        }
    }

}

enum class StorageStatus(val status: Int) {
    REMOTE(1), PROGRESS(2), LOCAL(3)
}
