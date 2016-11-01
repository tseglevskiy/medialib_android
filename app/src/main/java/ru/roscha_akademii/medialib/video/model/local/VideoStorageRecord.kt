package ru.roscha_akademii.medialib.video.model.local

class VideoStorageRecord {
    constructor(id: Long, downloadId: Long) {
        this.id = id
        this.downloadId = downloadId
    }

    constructor(id: Long, downloadId: Long, localUri: String?, status: StorageStatus, percent: Int?) {
        this.id = id
        this.localUri = localUri
        this.status = status
        this.downloadId = downloadId
        this.percent = percent
    }

    var id: Long = 0
    var downloadId: Long
    var localUri: String? = null
    var status: StorageStatus = StorageStatus.REMOTE
    var percent: Int? = null

    override fun toString(): String {
        return "VideoStorageRecord: $id $downloadId $status $localUri"
    }
}

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