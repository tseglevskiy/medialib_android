package ru.roscha_akademii.medialib.videocontrol.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.hannesdorfmann.mosby.mvp.layout.MvpFrameLayout;

import java.util.Formatter;
import java.util.Locale;

import ru.roscha_akademii.medialib.R;
import ru.roscha_akademii.medialib.databinding.VideocontrolBinding;
import ru.roscha_akademii.medialib.videocontrol.VideoControlCallback;
import ru.roscha_akademii.medialib.videocontrol.VideoControlInterface;
import ru.roscha_akademii.medialib.videocontrol.presenter.VideoControlPresenter;
import ru.roscha_akademii.medialib.videocontrol.presenter.VideoControlPresenterImpl;

/*
 * вдохновение и идеи для расшиpения этого класса можно брать в
 * com.google.android.exoplayer2.ui.PlaybackControlView
 */

public class VideoControl
        extends MvpFrameLayout<VideoControlView, VideoControlPresenter>
        implements VideoControlView, VideoControlInterface
{
    private final VideocontrolBinding binding;

    VideoControlCallback callback;

    public VideoControl(Context context) {
        this(context, null);
    }

    public VideoControl(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.videocontrol,
                this,
                true
        );

        binding.mediacontrollerProgress.setMax(PROGRESS_BAR_MAX);
        binding.mediacontrollerProgress.setOnSeekBarChangeListener(positionListener);

        binding.play.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getPresenter().gonnaPlay();
            }
        });
        binding.pause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getPresenter().gonnaPause();
            }
        });

        binding.fullscreen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getPresenter().gonnaFullScreen();
            }
        });
        binding.fullscreenExit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getPresenter().gonnaNormalScreen();
            }
        });
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        if (getPresenter() != null) {
            if (visibility == VISIBLE) {
                getPresenter().setIsVisible();
            } else {
                getPresenter().setIsInvisible();
            }
        }
    }

    @NonNull
    @Override
    public VideoControlPresenter createPresenter() {
        VideoControlPresenter presenter = new VideoControlPresenterImpl();

        presenter.setCallback(callback);
        presenter.setPlayer(player);
        if (getVisibility() == VISIBLE) {
            presenter.setIsVisible();
        } else {
            presenter.setIsInvisible();
        }

        return presenter;
    }

    @Override
    public void setCallback(VideoControlCallback callback) {
        this.callback = callback;
        if (getPresenter() != null) {
            getPresenter().setCallback(callback);
        }
    }

    /*
     * Player control
     *
     */

    ExoPlayer player = null;

    @Override
    public void setPlayer(ExoPlayer player) {
        this.player = player;
        if (getPresenter() != null) {
            getPresenter().setPlayer(player);
        }
    }

    @Override
    public void releasePlayer() {
        getPresenter().revokePlayer();
    }

    /*
     * Dragging in Progress bar
     *
     */

    boolean dragging = false;
    SeekBar.OnSeekBarChangeListener positionListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            dragging = true;
            if (callback != null) callback.pauseAutohide();
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                binding.timeCurrent.setText(stringForTime(filmDuration * progress / PROGRESS_BAR_MAX));
            }
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            dragging = false;
            getPresenter().seekTo(seekBar.getProgress(), seekBar.getMax());
            if (callback != null) callback.resumeAutohide();
        }
    };

    /*
     * Tools
     *
     */

    private static final int PROGRESS_BAR_MAX = 1000;
    private final StringBuilder formatBuilder = new StringBuilder();
    private final Formatter formatter = new Formatter(formatBuilder, Locale.getDefault());

    private String stringForTime(long timeMs) {
        if (timeMs == C.TIME_UNSET) {
            timeMs = 0;
        }
        long totalSeconds = (timeMs + 500) / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;

        formatBuilder.setLength(0);

        return hours > 0
                ? formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString()
                : formatter.format("%02d:%02d", minutes, seconds).toString();
    }

    private int progressBarValue(long position) {
        if (filmDuration == 0) return 0;
        return (int) ((position * PROGRESS_BAR_MAX) / filmDuration);
    }


    /*
     * VideoControlViewInterface
     *
     */

    long filmDuration = 0;

    @Override
    public void showTime(long filmDuration) {
        this.filmDuration = filmDuration;
        binding.time.setText(stringForTime(filmDuration));
    }

    @Override
    public void showCurrentTime(long position) {
        if (!dragging) {
            binding.timeCurrent.setText(stringForTime(position));
            binding.mediacontrollerProgress.setProgress(progressBarValue(position));
        }
    }

    @Override
    public void showBuffered(long bufferedPosition) {
        binding.mediacontrollerProgress.setSecondaryProgress(progressBarValue(bufferedPosition));
    }

    @Override
    public void setPlayPauseAction(PlayPauseMode mode) {
        if (mode == PlayPauseMode.PLAY) {
            binding.play.setVisibility(VISIBLE);
            binding.pause.setVisibility(GONE);

        } else if (mode == PlayPauseMode.PAUSE) {
            binding.play.setVisibility(GONE);
            binding.pause.setVisibility(VISIBLE);
        }
    }

    @Override
    public void setFullscreenAction(FullscreenMode mode) {
        if (mode == FullscreenMode.FULL) {
            binding.fullscreen.setVisibility(VISIBLE);
            binding.fullscreenExit.setVisibility(GONE);
        } else {
            binding.fullscreen.setVisibility(GONE);
            binding.fullscreenExit.setVisibility(VISIBLE);
        }
    }

}
