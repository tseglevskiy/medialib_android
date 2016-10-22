package ru.roscha_akademii.medialib.videocontrol.presenter;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import ru.roscha_akademii.medialib.videocontrol.VideoControlCallback;
import ru.roscha_akademii.medialib.videocontrol.view.VideoControlView;
import ru.roscha_akademii.medialib.viewvideo.view.StubExoPlayerEventListener;

public class VideoControlPresenterImpl
        extends MvpBasePresenter<VideoControlView>
        implements VideoControlPresenter
{
    private ExoPlayer player = null;
    private boolean isVisible = false;

    private ExoPlayer.EventListener playerEventListener = new StubExoPlayerEventListener() {
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
    private VideoControlCallback callback;

    @Override
    public void setCallback(VideoControlCallback callback) {
        this.callback = callback;
    }

    @Override
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

    @Override
    public void revokePlayer() {
        player.removeListener(playerEventListener);
        player = null;
    }

    /*
     * Commands from View
     *
     */

    @Override
    public void gonnaPlay() {
        if (player == null) return;

        player.setPlayWhenReady(true);

    }

    @Override
    public void gonnaPause() {
        if (player == null) return;

        player.setPlayWhenReady(false);
    }

    @Override
    public void gonnaFullScreen() {
        if (callback != null) {
            callback.gonnaFullScreen();
        }
    }

    @Override
    public void gonnaNormalScreen() {
        if (callback != null) {
            callback.gonnaNormalScreen();
        }    }

    @Override
    public void setIsVisible() {
        isVisible = true;
        updateAll();
    }

    @Override
    public void setIsInvisible() {
        isVisible = false;
    }

    @Override
    public void seekTo(int progress, int max) {
        if (player != null) {
            player.seekTo(positionValue(progress, max));
        }
    }

    private long positionValue(int progress, int max) {
        long duration =
                player == null
                        ? C.TIME_UNSET
                        : player.getDuration();
        return duration == C.TIME_UNSET
                ? 0
                : (duration * progress / max);
    }

    /*
     * Update view
     *
     */

    private void updateAll() {
        updateProgress();
        updatePlayPauseButton();
    }

    private void updateProgress() {
        if (!isVisible) return;
        if (getView() == null) return;

        long duration = player == null ? 0 : player.getDuration();
        long position = player == null ? 0 : player.getCurrentPosition();
        long bufferedPosition = player == null ? 0 : player.getBufferedPosition();

        getView().showTime(duration);
        getView().showCurrentTime(position);
        getView().showBuffered(bufferedPosition);

        updateProgressHandler.clear();

        // Schedule an update if necessary.
        int playbackState = player == null
                ? ExoPlayer.STATE_IDLE
                : player.getPlaybackState();

        if (playbackState != ExoPlayer.STATE_IDLE
                && playbackState != ExoPlayer.STATE_ENDED)
        {
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

    private void updatePlayPauseButton() {
        if (!isVisible) return;
        if (getView() == null) return;

        boolean playing = player != null && player.getPlayWhenReady();
        if (playing) {
            getView().setPlayPauseAction(VideoControlView.PlayPauseMode.PAUSE);
        } else {
            getView().setPlayPauseAction(VideoControlView.PlayPauseMode.PLAY);
        }
    }

    /*
     * Updater
     *
     */

    private ProgressHandler updateProgressHandler = new ProgressHandler();

    @SuppressLint("HandlerLeak")
    private class ProgressHandler extends Handler {
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
