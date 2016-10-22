package ru.roscha_akademii.medialib.videocontrol.presenter;

import com.google.android.exoplayer2.ExoPlayer;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import ru.roscha_akademii.medialib.videocontrol.VideoControlCallback;
import ru.roscha_akademii.medialib.videocontrol.view.VideoControlView;

public interface VideoControlPresenter extends MvpPresenter<VideoControlView> {
    void setCallback(VideoControlCallback callback);

    void setPlayer(ExoPlayer player);

    void revokePlayer();

    void gonnaPlay();

    void gonnaPause();

    void gonnaFullScreen();

    void gonnaNormalScreen();

    void setIsVisible();

    void setIsInvisible();

    void seekTo(int progress, int max);

}
