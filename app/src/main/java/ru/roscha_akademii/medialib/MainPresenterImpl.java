package ru.roscha_akademii.medialib;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

public class MainPresenterImpl
        extends MvpBasePresenter<MainView>
        implements MainPresenter
{
    @Override
    public void helloClicked() {
        if (isViewAttached()) {
            getView().showHelloToast();
        }
    }
}
