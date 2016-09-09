package ru.roscha_akademii.medialib;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;

/**
 * Created by tse on 09/09/16.
 */
public interface MainPresenter extends MvpPresenter<MainView> {
    void helloClicked();
}
