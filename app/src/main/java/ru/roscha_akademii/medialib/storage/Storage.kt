package ru.roscha_akademii.medialib.storage

interface Storage {
    fun getStatus(remoteUri: String): StorageStatus

    fun checkDownloadStatus(remoteUri: String)

    fun checkDownloadStatus(record: VideoStorageRecord)

    fun checkLocalUri(remoteUri: String)

    fun getPercent(remoteUri: String): Int

    fun saveLocal(remoteUri: String, title: String, visible: Boolean = false)

    fun removeLocal(remoteUri: String)

    fun getLocalUriIfAny(remoteUri: String): String

    fun cleanExceptThese(alive: Set<String>)
}