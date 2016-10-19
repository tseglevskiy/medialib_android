package ru.roscha_akademii.medialib.viewvideo.view;

import android.util.Log;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Timeline;

public class StubExoPlayerEventListener implements ExoPlayer.EventListener {
    @Override
    public void onLoadingChanged(boolean isLoading) {
        Log.d("happy", "event onLoadingChanged " + isLoading);
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        Log.d("happy", "event onPlayerStateChanged " + playWhenReady + " " + playbackState);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
        Log.d("happy", "event onTimelineChanged");
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Log.d("happy", "event onPlayerError " + error.getMessage());
    }

    @Override
    public void onPositionDiscontinuity() {
        Log.d("happy", "event onPositionDiscontinuity");
    }
}
