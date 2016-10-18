package ru.roscha_akademii.medialib.viewvideo.view;

import com.hannesdorfmann.mosby.mvp.MvpView;

import ru.roscha_akademii.medialib.net.model.Video;

public interface ShowVideoView extends MvpView {
    void showVideo(Video video);
}
