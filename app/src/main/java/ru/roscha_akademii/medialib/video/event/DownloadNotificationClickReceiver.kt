package ru.roscha_akademii.medialib.video.event

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ru.roscha_akademii.medialib.common.MediaLibApplication
import ru.roscha_akademii.medialib.common.ServiceNavigator
import javax.inject.Inject

class DownloadNotificationClickReceiver : BroadcastReceiver() {
//    @Inject
//    lateinit var videoStorage: VideoStorage

    @Inject
    lateinit var navigator : ServiceNavigator

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != DownloadManager.ACTION_NOTIFICATION_CLICKED) return

        (context.applicationContext as MediaLibApplication)
                .component
        .inject(this)

        navigator.startVideoList()
    }

}