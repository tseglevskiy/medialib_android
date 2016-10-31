package ru.roscha_akademii.medialib.update

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log

import com.pushtorefresh.storio.sqlite.StorIOSQLite

import java.util.ArrayList

import javax.inject.Inject
import javax.inject.Named

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.roscha_akademii.medialib.common.MediaLibApplication
import ru.roscha_akademii.medialib.video.model.remote.Video
import ru.roscha_akademii.medialib.video.model.remote.VideoAnswer
import ru.roscha_akademii.medialib.video.model.remote.VideoApi

class UpdateService : Service() {

    @Inject
    lateinit var updateScheduler: UpdateScheduler

    @Inject
    lateinit var api: VideoApi

    @field:[Inject Named("video db")]
    lateinit var videoDb: StorIOSQLite

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("happy", "UpdateService onCreate")

        (applicationContext as MediaLibApplication).component.inject(this)

        update()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("happy", "UpdateService onDestroy")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        return Service.START_NOT_STICKY
    }

    internal fun update() {
        val call = api.videoList()
        call.enqueue(object : Callback<VideoAnswer> {
            override fun onResponse(call: Call<VideoAnswer>, response: Response<VideoAnswer>) {
                try {
                    val answer = response.body()
                    Log.d("happy", "got videos " + answer.list?.size)

                    if (answer.list != null) {
                        saveVideos(answer.list!!)
                    }

                    updateScheduler.updateCompleted()
                } catch (ignore: Exception) {
                    // ничего, в следующий раз получится
                }

                stopSelf()
            }

            override fun onFailure(call: Call<VideoAnswer>, t: Throwable) {
                stopSelf()
            }
        })
    }

    internal fun saveVideos(list: ArrayList<Video>) {
        videoDb.put().objects(list).prepare().executeAsBlocking()

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
