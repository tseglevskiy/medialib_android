package ru.roscha_akademii.medialib.viewvideo.view

import android.util.Log
import android.view.Surface

import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.decoder.DecoderCounters

open class StubVideoListener : SimpleExoPlayer.VideoListener {
    override fun onVideoSizeChanged(width: Int,
                                    height: Int,
                                    unappliedRotationDegrees: Int,
                                    pixelWidthHeightRatio: Float) {
        Log.d("happy", "StubVideoListener onVideoSizeChanged" + width + " " + height
                + " " + unappliedRotationDegrees + " " + pixelWidthHeightRatio)
    }

    override fun onRenderedFirstFrame(surface: Surface) {
        Log.d("happy", "StubVideoListener onRenderedFirstFrame")
    }

    override fun onVideoDisabled(counters: DecoderCounters) {
        Log.d("happy", "StubVideoListener onVideoDisabled")
    }
}
