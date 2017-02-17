package ru.roscha_akademii.medialib.common

import android.app.Activity
import dagger.Module
import dagger.Provides
import org.greenrobot.eventbus.EventBus
import ru.roscha_akademii.medialib.mainscreen.presenter.MainScreenPresenter
import ru.roscha_akademii.medialib.storage.Storage
import ru.roscha_akademii.medialib.update.UpdateScheduler
import ru.roscha_akademii.medialib.video.model.local.VideoDb
import ru.roscha_akademii.medialib.video.playvideo.viewvideo.presenter.ShowVideoPresenterImpl
import ru.roscha_akademii.medialib.video.showlist.list.presenter.MainPresenterImpl

@Module
class ActivityModule(private val activity: Activity) {

    @Provides
    @ActivityScope
    internal fun providesActivityNavigator(): ActivityNavigator {
        return ActivityNavigator(activity)
    }

    @Provides
    @ActivityScope
    internal fun providesMainPresenter(bus: EventBus,
                                       videoDb: VideoDb,
                                       navigator: ActivityNavigator): MainPresenterImpl {
        return MainPresenterImpl(bus, videoDb, navigator)
    }

    @Provides
    @ActivityScope
    internal fun providesShowVideoPresenter(videoDb: VideoDb, storage: Storage): ShowVideoPresenterImpl {
        return ShowVideoPresenterImpl(videoDb, storage)
    }

    @Provides
    @ActivityScope
    internal fun provideMainScreenPresenter(scheduler: UpdateScheduler): MainScreenPresenter {
        return MainScreenPresenter(scheduler)
    }
}
