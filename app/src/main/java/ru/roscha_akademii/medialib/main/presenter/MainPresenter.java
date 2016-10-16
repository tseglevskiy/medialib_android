package ru.roscha_akademii.medialib.main.presenter;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import ru.roscha_akademii.medialib.main.view.MainView;

public interface MainPresenter extends MvpPresenter<MainView> {
    void wannaUpdateVideoList();

    void start();
}
