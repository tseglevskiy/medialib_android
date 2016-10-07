package ru.roscha_akademii.medialib;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;

public interface MainPresenter extends MvpPresenter<MainView> {
    void helloClicked();
}
