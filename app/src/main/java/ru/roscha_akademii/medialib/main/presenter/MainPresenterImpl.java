package ru.roscha_akademii.medialib.main.presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

import ru.roscha_akademii.medialib.common.ActivityNavigator;
import ru.roscha_akademii.medialib.main.view.MainView;
import ru.roscha_akademii.medialib.net.model.Video;
import ru.roscha_akademii.medialib.update.UpdateScheduler;
import ru.roscha_akademii.medialib.video.VideoDb;

public class MainPresenterImpl
        extends MvpBasePresenter<MainView>
        implements MainPresenter
{
    UpdateScheduler updateScheduler;
    private VideoDb videoDb;
    private ActivityNavigator navigator;

    public MainPresenterImpl(UpdateScheduler updateScheduler,
                             VideoDb videoDb,
                             ActivityNavigator navigator) {

        this.updateScheduler = updateScheduler;
        this.videoDb = videoDb;
        this.navigator = navigator;
    }

    @Override
    public void wannaUpdateVideoList() {
        updateScheduler.startNow();
        if (getView() != null) {
            getView().showHelloToast();
        }
    }

    @Override
    public void start() {
        updateScheduler.startBySchedule();

        getAndDisplayVideoList();
    }

    @Override
    public void wannaOpenVideo(long id) {
        navigator.openVideo(id);
    }

    private void getAndDisplayVideoList() {
        List<Video> list = videoDb.getAllVideo();
        if (getView() != null) {
            getView().showVideoList(list);
        }
    }
}
