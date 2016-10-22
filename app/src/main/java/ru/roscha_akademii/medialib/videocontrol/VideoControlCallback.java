package ru.roscha_akademii.medialib.videocontrol;

public interface VideoControlCallback {
    void pauseAutohide();

    void resumeAutohide();

    void onPlay();

    void onPause();

    void gonnaFullScreen();

    void gonnaNormalScreen();
}
