package ru.roscha_akademii.medialib.videocontrol.view;

import com.hannesdorfmann.mosby.mvp.MvpView;

public interface VideoControlView extends MvpView {
    void showTime(long duration);

    void showCurrentTime(long position);

    void showBuffered(long bufferedPosition);

    void setPlayPauseAction(PlayPauseMode mode);

    void setFullscreenAction(FullscreenMode mode);

    enum PlayPauseMode {
        PLAY, PAUSE;
    }

    public enum FullscreenMode {
        FULL, NORMAL
    }
}
