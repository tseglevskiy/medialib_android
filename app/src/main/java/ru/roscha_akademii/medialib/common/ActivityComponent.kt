package ru.roscha_akademii.medialib.common


import dagger.Subcomponent
import ru.roscha_akademii.medialib.video.playvideo.viewvideo.presenter.ShowVideoPresenterImpl
import ru.roscha_akademii.medialib.video.playvideo.viewvideo.view.ShowVideoActivity
import ru.roscha_akademii.medialib.video.showlist.list.presenter.MainPresenterImpl
import ru.roscha_akademii.medialib.video.showlist.list.view.ListOfVideoActivity

@ActivityScope
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {
    fun mainPresenter(): MainPresenterImpl

    fun showVideoPresenterImpl(): ShowVideoPresenterImpl

    fun inject(activity: ListOfVideoActivity)

    fun inject(activity: ShowVideoActivity)
}
