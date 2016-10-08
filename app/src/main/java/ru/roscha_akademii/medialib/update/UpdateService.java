package ru.roscha_akademii.medialib.update;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import javax.inject.Inject;

import ru.roscha_akademii.medialib.common.MediaLibApplication;

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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("happy", "UpdateService onDestroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateScheduler.updateCompleted(); // TODO temporary
        stopSelf(startId);

        return START_NOT_STICKY;
    }
}
