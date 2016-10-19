package ru.roscha_akademii.medialib.net;

import retrofit2.Call;
import retrofit2.http.GET;
import ru.roscha_akademii.medialib.net.model.VideoAnswer;


public interface VideoApi {
    @GET("app/src/androidTest/assets/video.json")
    Call<VideoAnswer> getVideoList();

}
