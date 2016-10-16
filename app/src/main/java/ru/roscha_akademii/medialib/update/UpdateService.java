package ru.roscha_akademii.medialib.update;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.roscha_akademii.medialib.common.MediaLibApplication;
import ru.roscha_akademii.medialib.net.VideoApi;
import ru.roscha_akademii.medialib.net.model.Video;
import ru.roscha_akademii.medialib.net.model.VideoAnswer;

import static ru.roscha_akademii.medialib.video.VideoDbModule.VIDEO_DB;

public class UpdateService extends Service {
    /**
     * Intent для запуска этого сервиса
     *
     * @param context - any context
     * @return intent
     */
    public static Intent getStartIntent(Context context) {
        return new Intent(context, UpdateService.class);
    }

    @Inject
    UpdateScheduler updateScheduler;

    @Inject
    VideoApi api;

    @Inject
    @Named(VIDEO_DB)
    StorIOSQLite videoDb;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("happy", "UpdateService onCreate");

        ((MediaLibApplication) getApplicationContext()).component().inject(this);

        update();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("happy", "UpdateService onDestroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_NOT_STICKY;
    }

    void update() {
        Call<VideoAnswer> call = api.getVideoList();
        call.enqueue(new Callback<VideoAnswer>() {
            @Override
            public void onResponse(Call<VideoAnswer> call, Response<VideoAnswer> response) {
                try {
                    VideoAnswer answer = response.body();
                    Log.d("happy", "got videos " + answer.list.size());

                    saveVideos(answer.list);

                    updateScheduler.updateCompleted();
                } catch (Exception ignore) {
                    // ничего, в следующий раз получится
                }
                stopSelf();
            }

            @Override
            public void onFailure(Call<VideoAnswer> call, Throwable t) {
                stopSelf();
            }
        });
    }

    void saveVideos(ArrayList<Video> list) {
        videoDb
                .put()
                .objects(list)
                .prepare()
                .executeAsBlocking();

    }
}
