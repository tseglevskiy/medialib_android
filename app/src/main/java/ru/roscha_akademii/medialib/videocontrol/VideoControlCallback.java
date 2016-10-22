package ru.roscha_akademii.medialib.videocontrol;

public interface VideoControlCallback {
    void pauseAutohide();

    void resumeAutohide();

    void gonnaFullScreen();

    void gonnaNormalScreen();
}
