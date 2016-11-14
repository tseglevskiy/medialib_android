package ru.roscha_akademii.medialib.storage.stub

import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ru.roscha_akademii.medialib.storage.Storage
import ru.roscha_akademii.medialib.storage.StorageStatus


class StorageStubTest() {
    lateinit var storage: Storage // SUT

    @Before
    fun setUp() {
        storage = StorageStub()
    }

    @Test
    fun emptyMethods() {

        storage.checkDownloadStatus("asdfadfs")
        storage.checkLocalUri("asdfasfd")
        storage.saveLocal("asdf", "qwer", true)
        storage.removeLocal("asdfasfd")
    }

    @Test
    fun getStatus() {
        assertEquals(StorageStatus.REMOTE, storage.getStatus("aaa"))
    }

    @Test
    fun getLocalUriIfAny() {
        val dummyUri = "asdfadsfadsf"
        assertEquals(dummyUri, storage.getLocalUriIfAny(dummyUri))
    }

    @Test
    fun getPercent() {
        assertEquals(0, storage.getPercent("asdfasdf"))
    }


}