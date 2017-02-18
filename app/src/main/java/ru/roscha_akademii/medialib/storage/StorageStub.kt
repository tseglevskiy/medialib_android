package ru.roscha_akademii.medialib.storage.stub

import ru.roscha_akademii.medialib.storage.Storage
import ru.roscha_akademii.medialib.storage.model.StorageStatus
import ru.roscha_akademii.medialib.storage.model.FileStorageRecord

class StorageStub : Storage {
    override fun getStatus(remoteUri: String): StorageStatus = StorageStatus.REMOTE

    override fun checkDownloadStatus(remoteUri: String) {}

    override fun checkDownloadStatus(record: FileStorageRecord) {}

    override fun checkLocalUri(remoteUri: String) {}

    override fun getPercent(remoteUri: String): Int = 0

    override fun saveLocal(remoteUri: String, title: String, visible: Boolean) {}

    override fun removeLocal(remoteUri: String) {}

    override fun getLocalUriIfAny(remoteUri: String): String = remoteUri

    override fun cleanExceptThese(alive: Set<String>) {}
}
