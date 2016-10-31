package ru.roscha_akademii.medialib.video.playvideo.viewvideo.tools

import android.util.Log

import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Timeline

open class StubExoPlayerEventListener : ExoPlayer.EventListener {
    override fun onLoadingChanged(isLoading: Boolean) {
        Log.d("happy", "event onLoadingChanged " + isLoading)
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        Log.d("happy", "event onPlayerStateChanged $playWhenReady $playbackState")
    }

    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?) {
        Log.d("happy", "event onTimelineChanged")
    }

    override fun onPlayerError(error: ExoPlaybackException) {
        Log.d("happy", "event onPlayerError " + error.message)
    }

    override fun onPositionDiscontinuity() {
        Log.d("happy", "event onPositionDiscontinuity")
    }
}
