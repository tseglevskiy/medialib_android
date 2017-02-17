package ru.roscha_akademii.medialib.video.model.remote

import com.pushtorefresh.storio.sqlite.StorIOSQLite

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.roscha_akademii.medialib.storage.Storage
import ru.roscha_akademii.medialib.update.UpdateCallback
import ru.roscha_akademii.medialib.video.model.remote.entity.Video
import ru.roscha_akademii.medialib.video.model.remote.entity.VideoAnswer
import java.util.*

/*
    @field:[Inject Named("video db")]
    lateinit var videoDb: StorIOSQLite

 */
public open class VideoUpdate(val videoApi: VideoApi,
                  val videoDb: StorIOSQLite,
                  val storage: Storage) {

    public open fun update(callback: UpdateCallback) {
        val call = videoApi.videoList()
        call.enqueue(object : Callback<VideoAnswer> {
            override fun onResponse(call: Call<VideoAnswer>, response: Response<VideoAnswer>) {
                try {
                    response.body()?.list?.let {
                        saveVideos(it)
                    }

                    callback.onSuccess()
                    return

                } catch (ignore: Exception) {
                    // ничего, в следующий раз получится
                }

                callback.onFail()
            }

            override fun onFailure(call: Call<VideoAnswer>, t: Throwable) {
                callback.onFail()
            }
        })
    }

    internal fun saveVideos(list: ArrayList<Video>) {
        videoDb
                .put()
                .objects(list)
                .prepare()
                .executeAsBlocking()

        list
                .map { it.pictureUrl }
                .filterNotNull()
                .forEach { storage.saveLocal(it, "image", false) }

        val listToSave = list
                .flatMap { listOf(it.videoUrl, it.pictureUrl) }
                .filterNotNull()
                .toSet()

        storage
                .cleanExceptThese(listToSave)
    }

}

