package ru.roscha_akademii.medialib.main.view;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import ru.roscha_akademii.medialib.net.model.Video;

public interface MainView extends MvpView {
    void showHelloToast();

    void showVideoList(List<Video> list);
}
