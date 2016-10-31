package ru.roscha_akademii.medialib.common

import android.app.Activity

import dagger.Module
import dagger.Provides
import ru.roscha_akademii.medialib.video.showlist.list.presenter.MainPresenter
import ru.roscha_akademii.medialib.video.showlist.list.presenter.MainPresenterImpl
import ru.roscha_akademii.medialib.update.UpdateScheduler
import ru.roscha_akademii.medialib.video.model.local.VideoDb
import ru.roscha_akademii.medialib.video.playvideo.viewvideo.presenter.ShowVideoPresenter
import ru.roscha_akademii.medialib.video.playvideo.viewvideo.presenter.ShowVideoPresenterImpl

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
