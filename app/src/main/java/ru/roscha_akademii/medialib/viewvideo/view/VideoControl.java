package ru.roscha_akademii.medialib.viewvideo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Timeline;

import java.util.Formatter;
import java.util.Locale;

import ru.roscha_akademii.medialib.R;
import ru.roscha_akademii.medialib.databinding.VideocontrolBinding;

public class VideoControl extends FrameLayout {
    private final VideocontrolBinding binding;
    private FullscreenButtonListener fullscreenButtonListener;

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

        binding.play.setOnClickListener(v -> togglePlayPause());
        binding.pause.setOnClickListener(v -> togglePlayPause());

        binding.fullscreen.setOnClickListener(v -> {
            if (fullscreenButtonListener != null) fullscreenButtonListener.onClick();
        });
        binding.fullscreenExit.setOnClickListener(v -> {
            if (fullscreenButtonListener != null) fullscreenButtonListener.onClick();
        });
    }

    /*
     * Full screen button and listener
     *
     */

    public void setFullscreenMode(boolean isFullscreen) {
        if (isFullscreen) {
            binding.fullscreen.setVisibility(GONE);
            binding.fullscreenExit.setVisibility(VISIBLE);
        } else {
            binding.fullscreen.setVisibility(VISIBLE);
            binding.fullscreenExit.setVisibility(GONE);
        }
    }

    public void setFullscreenButtonListener(FullscreenButtonListener listener) {
        fullscreenButtonListener = listener;
    }

    public interface FullscreenButtonListener {
        void onClick();
    }

    /*
     * Player control
     *
     */

    ExoPlayer player = null;

    ExoPlayer.EventListener playerEventListener = new StubExoPlayerEventListener() {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            updatePlayPauseButton();
            updateProgress();
        }

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {
            updateProgress();
        }

        @Override
        public void onPositionDiscontinuity() {
            updateProgress();
        }
    };

    public void setPlayer(ExoPlayer player) {
        if (this.player != null) {
            this.player.removeListener(playerEventListener);
        }
        this.player = player;
        if (player != null) {
            player.addListener(playerEventListener);
        }
        updateAll();
    }

    public void revokePlayer() {
        player.removeListener(playerEventListener);
        player = null;
    }

    private void togglePlayPause() {
        if (player == null) return;

        player.setPlayWhenReady(!player.getPlayWhenReady());
    }

    private void updateAll() {
        updateProgress();
    }

    private void updatePlayPauseButton() {
        if (!isVisible()) return;

        boolean playing = player != null && player.getPlayWhenReady();
        if (playing) {
            binding.play.setVisibility(GONE);
            binding.pause.setVisibility(VISIBLE);
        } else {
            binding.play.setVisibility(VISIBLE);
            binding.pause.setVisibility(GONE);
        }
    }

    private void updateProgress() {
        if (!isVisible()) return;

        long duration = player == null ? 0 : player.getDuration();
        long position = player == null ? 0 : player.getCurrentPosition();
        long bufferedPosition = player == null ? 0 : player.getBufferedPosition();


        binding.time.setText(stringForTime(duration));

//        if (!dragging) {
        binding.timeCurrent.setText(stringForTime(position));
        binding.mediacontrollerProgress.setProgress(progressBarValue(position));
//        }

        binding.mediacontrollerProgress.setSecondaryProgress(progressBarValue(bufferedPosition));

        updateProgressHandler.clear();

        // Schedule an update if necessary.
        int playbackState = player == null
                ? ExoPlayer.STATE_IDLE
                : player.getPlaybackState();

        if (playbackState != ExoPlayer.STATE_IDLE && playbackState != ExoPlayer.STATE_ENDED) {

            if (player.getPlayWhenReady()
                    && playbackState == ExoPlayer.STATE_READY)
            {
                long delayMs = 1000 - (position % 1000);
                if (delayMs < 200) delayMs += 1000;

                updateProgressHandler.update(delayMs);
            } else {
                updateProgressHandler.update(1000);

            }

        }
    }

    /*
     * Tools
     *
     */

    private static final int PROGRESS_BAR_MAX = 1000;
    private final StringBuilder formatBuilder = new StringBuilder();
    private final Formatter formatter = new Formatter(formatBuilder, Locale.getDefault());


    public boolean isVisible() {
        return getVisibility() == VISIBLE;
    }

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
        if (player == null) return 0;

        long duration = player.getDuration();
        if (duration == 0) return 0;

        return (int) ((position * PROGRESS_BAR_MAX) / duration);
    }

    /*
     * Updater
     *
     */

    ProgressHandler updateProgressHandler = new ProgressHandler();

    @SuppressLint("HandlerLeak")
    public class ProgressHandler extends Handler {
        private static final int MSG_UPDATE = 938373;

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_UPDATE) {
                updateProgress();
            } else {
                super.handleMessage(msg);
            }
        }

        void clear() {
            removeMessages(MSG_UPDATE);
        }

        void update(long delay) {
            sendEmptyMessageDelayed(MSG_UPDATE, delay);
        }
    }
}
