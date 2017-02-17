package ru.roscha_akademii.medialib.update

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import org.greenrobot.eventbus.EventBus
import ru.roscha_akademii.medialib.book.model.remote.BookUpdate
import ru.roscha_akademii.medialib.common.MediaLibApplication
import ru.roscha_akademii.medialib.update.event.DataDownloaded
import ru.roscha_akademii.medialib.video.model.remote.VideoUpdate
import javax.inject.Inject

class UpdateService : Service() {
    @Inject
    lateinit var videoUpdater: VideoUpdate

    @Inject
    lateinit var bookUpdater: BookUpdate

    @Inject
    lateinit var updateScheduler: UpdateScheduler

    @Inject
    lateinit var bus: EventBus

    val CHANNELS = 2
    var inProgress: Int = 0
    var success: Int = 0

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    val updateCallback = object : UpdateCallback {
        override fun onSuccess() {
            success++
            bus.post(DataDownloaded())
            somethingFinished()
        }

        override fun onFail() {
            somethingFinished()
        }

    }

    fun somethingFinished() {
        inProgress--

        if (inProgress == 0) {
            if (success == CHANNELS) {
                updateScheduler.updateCompleted()
            }

            stopSelf()
        }
    }

    fun startUpdate() {
        inProgress = CHANNELS
        success = 0

        videoUpdater.update(updateCallback)
        bookUpdater.update(updateCallback)
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("happy", "UpdateService onCreate")

        (applicationContext as MediaLibApplication).component.inject(this)

        startUpdate()

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("happy", "UpdateService onDestroy")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        return Service.START_NOT_STICKY
    }


    companion object {
        /**
         * Intent для запуска этого сервиса

         * @param context - any context
         * *
         * @return intent
         */
        fun getStartIntent(context: Context): Intent {
            return Intent(context, UpdateService::class.java)
        }
    }
}
