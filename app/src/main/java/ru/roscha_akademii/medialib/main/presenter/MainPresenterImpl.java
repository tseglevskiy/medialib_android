package ru.roscha_akademii.medialib.main.presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import ru.roscha_akademii.medialib.main.view.MainView;
import ru.roscha_akademii.medialib.update.UpdateScheduler;

public class MainPresenterImpl
        extends MvpBasePresenter<MainView>
        implements MainPresenter
{
    UpdateScheduler updateScheduler;

    public MainPresenterImpl(UpdateScheduler updateScheduler) {

        this.updateScheduler = updateScheduler;
    }

    @Override
    public void helloClicked() {
        updateScheduler.startNow();
        if (getView() != null) {
            getView().showHelloToast();
        }
    }

    @Override
    public void start() {
        updateScheduler.startBySchedule();
    }
}
