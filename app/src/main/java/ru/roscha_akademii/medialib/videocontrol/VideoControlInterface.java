package ru.roscha_akademii.medialib.videocontrol;

import com.google.android.exoplayer2.ExoPlayer;

import ru.roscha_akademii.medialib.videocontrol.view.VideoControlView;

public interface VideoControlInterface {
    void setPlayer(ExoPlayer player);

    void releasePlayer();

    void setCallback(VideoControlCallback callback);

    void setFullscreenAction(VideoControlView.FullscreenMode mode);
}
