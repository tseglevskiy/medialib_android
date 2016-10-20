package ru.roscha_akademii.medialib.viewvideo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.text.TextRenderer;
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

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/*
логика реализована сейчас такая:

вертикальное расположение - с меню и данными
горизонтальное - фулскрин, с убранными заголовками

пока не нажата "toggle full screen", телефон реагирует на вращения
если нажата - остаемся в горизонтальном расположении независимо от положения телефона
 */

public class ShowVideoActivity
        extends MvpActivity<ShowVideoView, ShowVideoPresenter>
        implements ShowVideoView
{
    private static final String EXTRA_ID = "id";

    private ShowvideoActivityBinding binding;
    private SimpleExoPlayer player;
    DataSource.Factory dataSourceFactory;
    ExtractorsFactory extractorsFactory;
    MediaSource videoSource;

    PlayerHandler playerHandler;
    HideControlHandler mainHandler;

    private View decorView;

    Mode mode = Mode.AUTO;

    long savedPosition = 0;
    private String url;

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


    SimpleExoPlayer.VideoListener videoListener = new StubVideoListener() {
        @Override
        public void onVideoSizeChanged(int width,
                                       int height,
                                       int unappliedRotationDegrees,
                                       float pixelWidthHeightRatio)
        {
            Log.d("happy", "video onVideoSizeChanged" + width + " " + height
                    + " " + unappliedRotationDegrees + " " + pixelWidthHeightRatio);

            binding.textureContainer
                    .setAspectRatio(height == 0 ? 1 : (width * pixelWidthHeightRatio) / height);
            binding.getRoot().requestLayout();
        }

    };

    ExoPlayer.EventListener eventListener = new StubExoPlayerEventListener();

    TextRenderer.Output textOutput = new StubTextRendererOutput();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((MediaLibApplication) getApplication())
                .component()
                .activityComponent(new ActivityModule(this))
                .inject(this);

        super.onCreate(savedInstanceState);

        Log.d("happy", "onCreate");

        binding = DataBindingUtil.setContentView(this, R.layout.showvideo_activity);
        binding.textureContainer.setAspectRatio(1.78f);

        playerHandler = new PlayerHandler();
        mainHandler = new HideControlHandler();

        binding.videoId.setOnClickListener(v -> doToggleFullscreen());

        // https://developer.android.com/training/system-ui/visibility.html
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener
                (visibility -> {
                    // Note that system bars will only be "visible" if none of the
                    // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        // The system bars are visible. Make any desired
                        // adjustments to your UI, such as showing the action bar or
                        // other navigational controls.

                        binding.videoId.setVisibility(VISIBLE);
                        mainHandler.hide();
                        binding.getRoot().requestLayout();
                    } else {
                        // The system bars are NOT visible. Make any desired
                        // adjustments to your UI, such as hiding the action bar or
                        // other navigational controls.

                        binding.videoId.setVisibility(GONE);
                        binding.getRoot().requestLayout();
                        mainHandler.clear();
                    }
                });

        checkOrientation();

        getPresenter().start(getVideoId());
    }

    @NonNull
    @Override
    public ShowVideoPresenter createPresenter() {
        return injectedPresenter;
    }

    @Override
    protected void onStart() {
        super.onStart();

        activatePlayer(url);
    }

    @Override
    protected void onStop() {
        super.onStop();

        deactivatePlayer();
    }

    @Override
    public void showVideo(Video video) {
        url = video.videoUrl;
    }

    public static class PlayerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.d("happy", "msg " + msg.what);

            super.handleMessage(msg);
        }
    }

    @SuppressLint("HandlerLeak")
    public class HideControlHandler extends Handler {
        static final int MSG_HIDE_CONTROLS = 1;

        @Override
        public void handleMessage(Message msg) {
            if (isLandscape()) {
                hideControls();
            }
        }

        void clear() {
            removeCallbacksAndMessages(null);
        }

        void hide() {
            clear();
            sendEmptyMessageDelayed(HideControlHandler.MSG_HIDE_CONTROLS, 5000);
        }
    }

    public void doToggleFullscreen() {
        if (mode == Mode.AUTO) {
            setMode(Mode.FULLSCREEN);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

//            hideControls();
            mainHandler.hide();
        } else {
            setMode(Mode.AUTO);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

//            showControls();
        }
    }

    void setMode(Mode mode) {
        this.mode = mode;
        if (mode == Mode.FULLSCREEN) {
            binding.videoId.setText("Normal");
        } else {
            binding.videoId.setText("Full screen");
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int newOrientation = newConfig.orientation;
        if (newOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            hideControls();
        } else {
            showControls();
        }
    }

    boolean isLandscape() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    void checkOrientation() {

        if (isLandscape()) {
            hideControls();

        } else {
            showControls();

        }
    }

    void showControls() {
        mainHandler.clear();

        if (getSupportActionBar() != null) {
            getSupportActionBar().show();
        }

        decorView.setSystemUiVisibility(0);
        getWindow().setFlags(
                0,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        binding.getRoot().requestLayout();
    }

    void hideControls() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // спрятать элементы навигации и включить фулскрин
        int visibilityFlags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        if (Build.VERSION.SDK_INT >= 19) {
            visibilityFlags |= View.SYSTEM_UI_FLAG_IMMERSIVE;
        }
        getWindow().getDecorView().setSystemUiVisibility(visibilityFlags);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding.videoId.setVisibility(GONE);
        binding.getRoot().requestLayout();
    }

    enum Mode {
        FULLSCREEN,
        AUTO
    }

    /*
    Player
     */


    private void activatePlayer(String url) {

        player = ExoPlayerFactory.newSimpleInstance(
                this,                                   // контекст
                new DefaultTrackSelector(playerHandler),  // TrackSelector
                new DefaultLoadControl());

//        binding.player.setPlayer(player);

        player.setVideoTextureView(binding.texture);

        player.setVideoListener(videoListener);
        player.addListener(eventListener);
        player.setTextOutput(textOutput);

        dataSourceFactory = new DefaultDataSourceFactory(
                this,               // Context
                "MyExoPlayerDemo"   // user-agent
        );

        extractorsFactory = new DefaultExtractorsFactory();

        videoSource = new ExtractorMediaSource(
                Uri.parse(url),     // uri источника
                dataSourceFactory,  // DataSource.Factory
                extractorsFactory,  // ExtractorsFactory
                playerHandler,        // Handler для получения событий от плеера
                null                // Listener - объект-получатель событий от плеера
        );

        player.prepare(videoSource);
        player.seekTo(savedPosition);

        player.setPlayWhenReady(true);

    }

    private void deactivatePlayer() {

        player.stop();

        savedPosition = player.getCurrentPosition();

        playerHandler.removeCallbacksAndMessages(null);

        player.release();

        player.setVideoListener(null);
        player.removeListener(eventListener);
        player.setTextOutput(null);

        ((MediaLibApplication) getApplication()).refWatcher().watch(videoSource);
        videoSource = null;

        ((MediaLibApplication) getApplication()).refWatcher().watch(dataSourceFactory);
        dataSourceFactory = null;

        ((MediaLibApplication) getApplication()).refWatcher().watch(extractorsFactory);
        extractorsFactory = null;

        ((MediaLibApplication) getApplication()).refWatcher().watch(player);
        player = null;

    }
}
