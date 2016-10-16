package ru.roscha_akademii.medialib.main.presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import ru.roscha_akademii.medialib.main.view.MainView;
import ru.roscha_akademii.medialib.update.UpdateScheduler;
import ru.roscha_akademii.medialib.video.VideoDb;

public class MainPresenterImpl
        extends MvpBasePresenter<MainView>
        implements MainPresenter
{
    UpdateScheduler updateScheduler;
    private VideoDb videoDb;

    public MainPresenterImpl(UpdateScheduler updateScheduler,
                             VideoDb videoDb)
    {

        this.updateScheduler = updateScheduler;
        this.videoDb = videoDb;
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
