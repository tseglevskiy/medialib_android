package ru.roscha_akademii.medialib.viewvideo.view;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import javax.inject.Inject;

import ru.roscha_akademii.medialib.R;
import ru.roscha_akademii.medialib.common.ActivityModule;
import ru.roscha_akademii.medialib.common.MediaLibApplication;
import ru.roscha_akademii.medialib.databinding.ShowvideoActivityBinding;
import ru.roscha_akademii.medialib.net.model.Video;
import ru.roscha_akademii.medialib.viewvideo.presenter.ShowVideoPresenter;

public class ShowVideoActivity
        extends MvpActivity<ShowVideoView, ShowVideoPresenter>
        implements ShowVideoView
{
    private static final String EXTRA_ID = "id";

    private ShowvideoActivityBinding binding;
    private SimpleExoPlayer player;
    DataSource.Factory dataSourceFactory;
    ExtractorsFactory extractorsFactory;
    PlayerHandler mainHandler;

    public static Intent getStartIntent(Context context, long id) {
        Intent intent = new Intent(context, ShowVideoActivity.class);
        intent.putExtra(EXTRA_ID, id);
        return intent;
    }

    public long getVideoId() {
        return getIntent().getLongExtra(EXTRA_ID, -1);
    }

    @Inject
    ShowVideoPresenter injectedPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((MediaLibApplication) getApplication())
                .component()
                .activityComponent(new ActivityModule(this))
                .inject(this);

        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.showvideo_activity);
        binding.videoId.setText("" + getVideoId());

        mainHandler = new PlayerHandler();

        player = ExoPlayerFactory.newSimpleInstance(
                this,                                   // контекст
                new DefaultTrackSelector(mainHandler),  // TrackSelector
                new DefaultLoadControl());
        binding.player.setPlayer(player);

        dataSourceFactory = new DefaultDataSourceFactory(
                this,               // Context
                "MyExoPlayerDemo"   // user-agent
        );

        extractorsFactory = new DefaultExtractorsFactory();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ((MediaLibApplication) getApplication()).refWatcher().watch(injectedPresenter);
        ((MediaLibApplication) getApplication()).refWatcher().watch(player);
    }

    @NonNull
    @Override
    public ShowVideoPresenter createPresenter() {
        return injectedPresenter;
    }

    @Override
    protected void onStart() {
        super.onStart();

        getPresenter().start(getVideoId());
    }

    @Override
    public void showVideo(Video video) {
        MediaSource videoSource = new ExtractorMediaSource(
                Uri.parse(video.videoUrl),     // uri источника
                dataSourceFactory,  // DataSource.Factory
                extractorsFactory,  // ExtractorsFactory
                mainHandler,        // Handler для получения событий от плеера
                null                // Listener - объект-получатель событий от плеера
        );

        player.prepare(videoSource);
        player.setPlayWhenReady(true);

    }

    public static class PlayerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.d("happy", "msg " + msg.what);

            super.handleMessage(msg);
        }
    }


}
