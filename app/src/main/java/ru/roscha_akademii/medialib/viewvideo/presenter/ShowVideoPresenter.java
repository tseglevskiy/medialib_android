package ru.roscha_akademii.medialib.viewvideo.presenter;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import ru.roscha_akademii.medialib.viewvideo.view.ShowVideoView;

public interface ShowVideoPresenter extends MvpPresenter<ShowVideoView> {
    void start(long videoId);
}
