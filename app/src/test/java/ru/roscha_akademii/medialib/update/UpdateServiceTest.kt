package ru.roscha_akademii.medialib.update

import android.content.SharedPreferences
import com.nhaarman.mockito_kotlin.*
import com.pushtorefresh.storio.sqlite.StorIOSQLite
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowService
import org.robolectric.util.ServiceController
import ru.roscha_akademii.medialib.BuildConfig
import ru.roscha_akademii.medialib.book.BookModule
import ru.roscha_akademii.medialib.book.model.remote.BookApi
import ru.roscha_akademii.medialib.book.model.remote.BookUpdate
import ru.roscha_akademii.medialib.common.*
import ru.roscha_akademii.medialib.storage.Storage
import ru.roscha_akademii.medialib.video.model.VideoDbModule
import ru.roscha_akademii.medialib.video.model.local.VideoDb
import ru.roscha_akademii.medialib.video.model.remote.VideoApi
import ru.roscha_akademii.medialib.video.model.remote.VideoUpdate


@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class,
        sdk = intArrayOf(21),
        manifest = "AndroidManifest.xml",
        application = RobolectricMdiaLibApplication::class,
        packageName = "ru.roscha_akademii.medialib")

class UpdateServiceTest {
    lateinit var serviceController: ServiceController<UpdateService> // SUT
    lateinit var shadow: ShadowService

    lateinit var videoUpdate: VideoUpdate
    lateinit var bookUpdate: BookUpdate
    lateinit var updateScheduler: UpdateScheduler

    @Before
    fun setUp() {
        videoUpdate = mock<VideoUpdate>()
        bookUpdate = mock<BookUpdate>()
        updateScheduler = mock<UpdateScheduler>()

        val app = RuntimeEnvironment.application as RobolectricMdiaLibApplication

        val component = DaggerApplicationComponent
                .builder()
                .androidModule(AndroidModule(app, app.refWatcher))
                .videoDbModule(object : VideoDbModule() {
                    override fun providesVideoDbFileName() = "" // in-memory database for tests

                    override fun providesVideoUpdate(videoApi: VideoApi, videoDb: VideoDb, storage: Storage)
                            = videoUpdate
                })
                .bookModule(object : BookModule() {
                    override fun providesBookUpdate(api: BookApi)
                            = bookUpdate
                })
                .updateModule(object : UpdateModule() {
                    override fun provides(prefs: SharedPreferences, timeProvider: TimeProvider, navigator: ServiceNavigator)
                            = updateScheduler

                })
                .build()

        app.setTestComponent(component)

        serviceController = Robolectric
                .buildService(UpdateService::
                class.java)

        shadow = shadowOf(serviceController.get())
    }

    @Test
    fun chеckCallback() {
        val callback: UpdateCallback = mock<UpdateCallback>()

        callback.onSuccess()

        verify<UpdateCallback>(callback).onSuccess()
        verify<UpdateCallback>(callback, times(1)).onSuccess()
        verify<UpdateCallback>(callback, times(0)).onFail()
    }

    @Test
    fun checkVideoUpdater() {
        val callback: UpdateCallback = mock<UpdateCallback>()

        videoUpdate.update(callback)

        verify(videoUpdate).update(any())
    }

    @Test
    fun checkBookUpdater() {
        val callback: UpdateCallback = mock<UpdateCallback>()

        bookUpdate.update(callback)

        verify(bookUpdate).update(any())
    }

    @Test
    fun startUpdateWhenTestStarts() {
        serviceController.create()

        verify(videoUpdate, times(1)).update(any())
        verify(bookUpdate, times(1)).update(any())

        assert(!shadow.isStoppedBySelf)
    }

    fun runUpdateCallbacks(first: String, second: String) {
        serviceController.create()

        val callbackCaptor1 = ArgumentCaptor.forClass(UpdateCallback::class.java)
        val callbackCaptor2 = ArgumentCaptor.forClass(UpdateCallback::class.java)

        verify(videoUpdate, times(1)).update(capture(callbackCaptor1))
        verify(bookUpdate, times(1)).update(capture(callbackCaptor2))

        val callback1 = callbackCaptor1.allValues[0]
        val callback2 = callbackCaptor2.allValues[0]

        when (first) {
            "success" -> callback1.onSuccess()
            "fail" -> callback1.onFail()
        }

        when (second) {
            "success" -> callback2.onSuccess()
            "fail" -> callback2.onFail()
        }
    }

    @Test
    fun continueWhenUpdate1() {
        runUpdateCallbacks("no", "no")

        assert(!shadow.isStoppedBySelf)
    }

    @Test
    fun continueWhenUpdate2() {
        runUpdateCallbacks("no", "success")

        assert(!shadow.isStoppedBySelf)
    }

    @Test
    fun continueWhenUpdate3() {
        runUpdateCallbacks("success", "no")

        assert(!shadow.isStoppedBySelf)
    }

    @Test
    fun stopAfterUpdate() {
        runUpdateCallbacks("success", "success")

        assert(shadow.isStoppedBySelf)
    }

    @Test
    fun stopAfterUpdate2() {
        runUpdateCallbacks("fail", "fail")

        assert(shadow.isStoppedBySelf)
    }

    @Test
    fun allUpdatesSuccess() {
        runUpdateCallbacks("success", "success")

        verify(updateScheduler, times(1)).updateCompleted()
    }

    @Test
    fun firstUpdateSuccess() {
        runUpdateCallbacks("success", "fail")

        verify(updateScheduler, times(0)).updateCompleted()
    }

    @Test
    fun secondUpdateSuccess() {
        runUpdateCallbacks("fail", "success")

        verify(updateScheduler, times(0)).updateCompleted()
    }

    @Test
    fun allUpdatesFail() {
        runUpdateCallbacks("fail", "fail")

        verify(updateScheduler, times(0)).updateCompleted()
    }

}