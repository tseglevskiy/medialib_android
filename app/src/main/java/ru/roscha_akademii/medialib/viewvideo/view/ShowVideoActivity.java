package ru.roscha_akademii.medialib.viewvideo.view;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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

    private int savedOrientation;
    private ViewGroup.LayoutParams originalContainerLayoutParams;
    private boolean isFullscreen = false;
    private View container;

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
//        binding.videoId.setText("" + getVideoId());

        mainHandler = new PlayerHandler();

        player = ExoPlayerFactory.newSimpleInstance(
                this,                                   // контекст
                new DefaultTrackSelector(mainHandler),  // TrackSelector
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

//        container = binding.container;
        container = binding.textureContainer;

        originalContainerLayoutParams = container.getLayoutParams();
        binding.videoId.setOnClickListener(v -> doToggleFullscreen());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        player.stop();
        player.setVideoTextureView(null);
        player.setVideoListener(null);
        player.removeListener(eventListener);
        player.setTextOutput(null);

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

    public void doToggleFullscreen() {
        if (isFullscreen) {
            Log.d("happy", "go normal");
            setRequestedOrientation(savedOrientation);

            getSupportActionBar().show();

            // Make the status bar and navigation bar visible again.
            getWindow().getDecorView().setSystemUiVisibility(0);

            container.setLayoutParams(originalContainerLayoutParams);

            getWindow().setFlags(
                    0, // clear flag
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            container.setLayoutParams(getLayoutParamsBasedOnParent(
                    container,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));


//            fullscreenButton.setImageResource(R.drawable.ic_action_full_screen);

            isFullscreen = false;
        } else {
            Log.d("happy", "go full");
            savedOrientation = getResources().getConfiguration().orientation;

            getSupportActionBar().hide();

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);

//            requestWindowFeature(Window.FEATURE_NO_TITLE);

            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            container.setLayoutParams(getLayoutParamsBasedOnParent(
                    container,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

//            fullscreenButton.setImageResource(R.drawable.ic_action_return_from_full_screen);

            isFullscreen = true;
        }

    }

    public static ViewGroup.LayoutParams getLayoutParamsBasedOnParent(View view, int width, int height)
            throws IllegalArgumentException
    {

        // Get the parent of the given view.
        ViewParent parent = view.getParent();

        // Determine what is the parent's type and return the appropriate type of LayoutParams.
        if (parent instanceof FrameLayout) {
            return new FrameLayout.LayoutParams(width, height);
        }
        if (parent instanceof RelativeLayout) {
            return new RelativeLayout.LayoutParams(width, height);
        }
        if (parent instanceof LinearLayout) {
            return new LinearLayout.LayoutParams(width, height);
        }

        // Throw this exception if the parent is not the correct type.
        IllegalArgumentException exception = new IllegalArgumentException("The PARENT of a " +
                "FrameLayout container used by the GoogleMediaFramework must be a LinearLayout, " +
                "FrameLayout, or RelativeLayout. Please ensure that the container is inside one of these " +
                "three supported view groups.");

        // If the parent is not one of the supported types, throw our exception.
        throw exception;
    }

}
