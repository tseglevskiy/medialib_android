package ru.roscha_akademii.medialib.viewvideo.view;

import android.util.Log;
import android.view.Surface;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.decoder.DecoderCounters;

public class StubVideoListener implements SimpleExoPlayer.VideoListener {
    @Override
    public void onVideoSizeChanged(int width,
                                   int height,
                                   int unappliedRotationDegrees,
                                   float pixelWidthHeightRatio)
    {
        Log.d("happy", "StubVideoListener onVideoSizeChanged" + width + " " + height
                + " " + unappliedRotationDegrees + " " + pixelWidthHeightRatio);
    }

    @Override
    public void onRenderedFirstFrame(Surface surface) {
        Log.d("happy", "StubVideoListener onRenderedFirstFrame");
    }

    @Override
    public void onVideoDisabled(DecoderCounters counters) {
        Log.d("happy", "StubVideoListener onVideoDisabled");
    }
}
