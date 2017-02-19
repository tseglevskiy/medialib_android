package ru.roscha_akademii.medialib.common

import dagger.Subcomponent
import ru.roscha_akademii.medialib.book.showlist.list.presenter.BookListPresenter
import ru.roscha_akademii.medialib.mainscreen.presenter.MainScreenPresenter
import ru.roscha_akademii.medialib.video.playvideo.viewvideo.presenter.ShowVideoPresenterImpl
import ru.roscha_akademii.medialib.video.playvideo.viewvideo.view.ShowVideoActivity
import ru.roscha_akademii.medialib.video.showlist.list.presenter.MainPresenterImpl
import ru.roscha_akademii.medialib.video.showlist.list.view.ListOfVideoFragment

@ActivityScope
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {
    fun mainPresenter(): MainPresenterImpl

    fun bookListPresenter(): BookListPresenter

    fun showVideoPresenterImpl(): ShowVideoPresenterImpl

    fun mainScreenPresenter(): MainScreenPresenter

    fun inject(fragment: ListOfVideoFragment)

    fun inject(activity: ShowVideoActivity)
}
