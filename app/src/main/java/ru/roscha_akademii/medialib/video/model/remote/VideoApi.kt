package ru.roscha_akademii.medialib.video.model.remote

import retrofit2.Call
import retrofit2.http.GET
import ru.roscha_akademii.medialib.video.model.remote.entity.VideoAnswer

interface VideoApi {
    @GET("app/src/androidTest/assets/video.json")
    fun videoList(): Call<VideoAnswer>
}
