package ru.roscha_akademii.medialib.net;

import retrofit2.Call;
import retrofit2.http.GET;
import ru.roscha_akademii.medialib.net.model.VideoAnswer;


public interface VideoApi {
    @GET("raw/87aff1ef294055d83b429a29c981b9514211a8bc/app/src/androidTest/assets/video.json")
    Call<VideoAnswer> getVideoList();

}
