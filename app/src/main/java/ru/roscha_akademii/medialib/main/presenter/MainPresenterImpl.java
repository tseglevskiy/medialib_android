package ru.roscha_akademii.medialib.main.presenter;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;

import ru.roscha_akademii.medialib.common.MediaLibApplication;
import ru.roscha_akademii.medialib.main.view.MainView;
import ru.roscha_akademii.medialib.update.UpdateScheduler;

public class MainPresenterImpl
        extends MvpBasePresenter<MainView>
        implements MainPresenter
{
    private Context context;

    @Inject
    UpdateScheduler updateScheduler;

    public MainPresenterImpl(Context context) {
        this.context = context.getApplicationContext();
        ((MediaLibApplication) this.context).component().inject(this);
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
