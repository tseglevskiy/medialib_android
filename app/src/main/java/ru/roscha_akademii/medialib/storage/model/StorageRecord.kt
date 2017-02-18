package ru.roscha_akademii.medialib.storage.model

data class FileStorageRecord(val remoteUri: String,
                             val downloadId: Long,
                             var localUri: String? = null,
                             var status: StorageStatus = StorageStatus.REMOTE,
                             var percent: Int = 0)

enum class StorageStatus(val value: Int) {
    REMOTE(1), PROGRESS(2), LOCAL(3);

    companion object {
        private val map = values().associateBy(StorageStatus::value)

        fun fromInt(type: Int?): StorageStatus {
            return try {
                map[type] ?: REMOTE
            } catch (e: IndexOutOfBoundsException) {
                REMOTE
            }
        }
    }
}