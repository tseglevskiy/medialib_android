package ru.roscha_akademii.medialib.video.model.local

data class VideoStorageRecord (val id: Long,
                               val downloadId: Long,
                               var localUri: String? = null,
                               var status: StorageStatus = StorageStatus.REMOTE,
                               var percent: Int? = null)

enum class StorageStatus(val value: Int) {
    REMOTE(1), PROGRESS(2), LOCAL(3);

    companion object {
        private val map = StorageStatus.values().associateBy(StorageStatus::value)

        fun fromInt(type: Int?): StorageStatus {
            return try {
                map[type] ?: REMOTE
            } catch (e: IndexOutOfBoundsException) {
                REMOTE
            }
        }
    }
}