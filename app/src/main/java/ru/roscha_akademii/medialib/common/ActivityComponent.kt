package ru.roscha_akademii.medialib.common


import dagger.Subcomponent
import ru.roscha_akademii.medialib.video.showlist.list.presenter.MainPresenter
import ru.roscha_akademii.medialib.video.showlist.list.view.ListOfVideoActivity
import ru.roscha_akademii.medialib.video.playvideo.viewvideo.view.ShowVideoActivity
import ru.roscha_akademii.medialib.video.showlist.list.presenter.MainPresenterImpl

@ActivityScope
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {
    fun mainPresenter(): MainPresenterImpl

    fun inject(activity: ListOfVideoActivity)

    fun inject(activity: ShowVideoActivity)
}
