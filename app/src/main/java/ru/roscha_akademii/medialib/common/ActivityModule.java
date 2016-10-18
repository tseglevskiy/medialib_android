package ru.roscha_akademii.medialib.common;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;
import ru.roscha_akademii.medialib.main.presenter.MainPresenter;
import ru.roscha_akademii.medialib.main.presenter.MainPresenterImpl;
import ru.roscha_akademii.medialib.update.UpdateScheduler;
import ru.roscha_akademii.medialib.video.VideoDb;
import ru.roscha_akademii.medialib.viewvideo.presenter.ShowVideoPresenter;
import ru.roscha_akademii.medialib.viewvideo.presenter.ShowVideoPresenterImpl;

@Module
public class ActivityModule {
    private Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    ActivityNavigator providesActivityNavigator() {
        return new ActivityNavigator(activity);
    }

    @Provides
    @ActivityScope
    MainPresenter providesMainPresenter(UpdateScheduler scheduler,
                                        VideoDb videoDb,
                                        ActivityNavigator navigator) {
        return new MainPresenterImpl(scheduler, videoDb, navigator);
    }

    @Provides
    @ActivityScope
    ShowVideoPresenter providesShowVideoPresenter() {
        return new ShowVideoPresenterImpl();
    }


}
