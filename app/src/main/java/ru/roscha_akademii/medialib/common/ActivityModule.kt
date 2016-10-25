package ru.roscha_akademii.medialib.common

import android.app.Activity

import dagger.Module
import dagger.Provides
import ru.roscha_akademii.medialib.main.presenter.MainPresenter
import ru.roscha_akademii.medialib.main.presenter.MainPresenterImpl
import ru.roscha_akademii.medialib.update.UpdateScheduler
import ru.roscha_akademii.medialib.video.VideoDb
import ru.roscha_akademii.medialib.viewvideo.presenter.ShowVideoPresenter
import ru.roscha_akademii.medialib.viewvideo.presenter.ShowVideoPresenterImpl

@Module
class ActivityModule(private val activity: Activity) {

    @Provides
    @ActivityScope
    internal fun providesActivityNavigator(): ActivityNavigator {
        return ActivityNavigator(activity)
    }

    @Provides
    @ActivityScope
    internal fun providesMainPresenter(scheduler: UpdateScheduler,
                                       videoDb: VideoDb,
                                       navigator: ActivityNavigator): MainPresenter {
        return MainPresenterImpl(scheduler, videoDb, navigator)
    }

    @Provides
    @ActivityScope
    internal fun providesShowVideoPresenter(videoDb: VideoDb): ShowVideoPresenter {
        return ShowVideoPresenterImpl(videoDb)
    }


}
